package ru.gareev.utilservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gareev.utilservice.api.dto.ConcurrentAccessResponse;
import ru.gareev.utilservice.service.DocumentService;

@RestController
@RequestMapping("/api/util")
@RequiredArgsConstructor
public class UtilController {

    DocumentService service;

    @Autowired
    public UtilController(DocumentService service){
        this.service = service;
    }

    @GetMapping("/concurrentApprove")
    public ResponseEntity<ConcurrentAccessResponse> getConcurrent(
            @RequestParam Long documentId,
            @RequestParam Long threads,
            @RequestParam Long attempts
    ) {
        //TODO add logic
        return ResponseEntity.ok(ConcurrentAccessResponse.builder().build());
    }

    @GetMapping("/ping")
    public ResponseEntity<?> pingDocuments() {
        if (service.ping()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/create")
    public ResponseEntity<Void> createDocuments(@RequestParam Long documentsCount){
        service.createDocuments(documentsCount);
        return ResponseEntity.ok().build();
    }
}
