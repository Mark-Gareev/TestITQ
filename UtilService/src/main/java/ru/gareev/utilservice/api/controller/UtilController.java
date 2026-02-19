package ru.gareev.utilservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gareev.utilservice.api.dto.ConcurrentAccessResponse;
import ru.gareev.utilservice.service.DocumentService;
import ru.gareev.utilservice.service.concurrent.ConcurrentApproveExecutor;

@RestController
@RequestMapping("/api/util")
@RequiredArgsConstructor
public class UtilController {

    private final DocumentService service;
    private final ConcurrentApproveExecutor approveExecutor;

    @GetMapping("/concurrentApprove")
    public ConcurrentAccessResponse getConcurrent(
            @RequestParam Long documentId,
            @RequestParam int threads,
            @RequestParam int attempts
    ) {
        //TODO add logic
        return approveExecutor.executeEveryThread(threads,attempts,documentId);
    }

    @GetMapping("/create")
    public ResponseEntity<Void> createDocuments(@RequestParam Long documentsCount){
        service.createDocuments(documentsCount);
        return ResponseEntity.ok().build();
    }
}
