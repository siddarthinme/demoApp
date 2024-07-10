package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DemoController {

    @Autowired
    private MessageRepository messageRepository;

    // Endpoint to return a hello world message
    @RequestMapping("/")
    public ResponseEntity<String> helloworld() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    // Endpoint to receive data and save it to the database
    @PostMapping("/data")
    public ResponseEntity<String> receiveData(@RequestBody String data) {
        try {
            Message message = new Message();
            message.setMessage(data);
            messageRepository.save(message);
            return new ResponseEntity<>("Data received and saved", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get all messages from the database
    @GetMapping("/data")
    public ResponseEntity<List<Message>> getData() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // New endpoint to add a message directly to the database
    @PostMapping("/addMessage")
    public ResponseEntity<String> addMessage(@RequestBody Message message) {
        try {
            messageRepository.save(message);
            return new ResponseEntity<>("Message added to database", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to update a message by ID
    @PutMapping("/editMessage/{id}")
    public ResponseEntity<String> updateMessage(@PathVariable Long id, @RequestBody String newData) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessage(newData);
            messageRepository.save(message);
            return new ResponseEntity<>("Message updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a message by ID
    @DeleteMapping("/deleteMessage/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
        }
    }

    // New endpoint to get a message by ID
    @GetMapping("/getMessage/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return new ResponseEntity<>(optionalMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
