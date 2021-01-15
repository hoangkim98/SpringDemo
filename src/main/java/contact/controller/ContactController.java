package contact.controller;

import contact.model.Contact;
import contact.model.ContactModelAssembler;
import contact.exception.ContactNotFoundException;
import contact.repository.ContactRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ContactController  {
    private final ContactRepository repository;
    private final ContactModelAssembler assembler;

    ContactController(ContactRepository repository, ContactModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/contacts")
    public CollectionModel<EntityModel<Contact>> all() {
        List<EntityModel<Contact>> contacts = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(contacts, linkTo(methodOn(ContactController.class).all()).withSelfRel());
    }

    @PostMapping("/contacts")
    Contact addNewContact(@RequestBody Contact newContact){
        return repository.save(newContact);
    }

    @GetMapping("contacts/{id}")
    public EntityModel<Contact> one(@PathVariable Long id){
        Contact contact =  repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
        return assembler.toModel(contact);
    }

    @PutMapping("/contacts/{id}")
    Contact replaceContact(@RequestBody Contact newContact, @PathVariable Long id){
        return repository.findById(id)
                .map(contact -> {
                    contact.setName(newContact.getName());
                    contact.setEmail(newContact.getEmail());
                    return repository.save(contact);
                        })
                .orElseGet(()->{
                    newContact.setId(id);
                    return repository.save(newContact);
                });
    }

    @DeleteMapping("/contacts/{id}")
    void deleteContact(@PathVariable Long id){
        repository.deleteById(id);
    }
}
