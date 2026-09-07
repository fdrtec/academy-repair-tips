package br.com.fdrtec.repair_tips_api.service;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " não encontrada com id " + id);
    }
}
