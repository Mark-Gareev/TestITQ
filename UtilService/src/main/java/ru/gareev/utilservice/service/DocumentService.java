package ru.gareev.utilservice.service;

public interface DocumentService {
    boolean ping();

    void createDocuments(Long documentsCount);
}
