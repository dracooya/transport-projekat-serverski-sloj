package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Document;
import rs.ac.uns.ftn.transport.repository.DocumentRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IDocumentService;

import java.util.Set;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Set<Document> findAllByDriver_Id(Integer id) {
        return documentRepository.findAllByDriver_Id(id);
    }

    public Integer deleteAllByDriver_Id(Integer id) {
        return documentRepository.deleteAllByDriver_Id(id);
    }

    public void deleteById(Integer id) {
        documentRepository.deleteById(id);
    }
}
