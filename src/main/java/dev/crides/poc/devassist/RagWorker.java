package dev.crides.poc.devassist;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RagWorker {

    private final VectorStore vectorStore;
    private final Resource textResource;

    public RagWorker(VectorStore vectorStore,
                     @Value("classpath:/recipe_train.txt") Resource pdfResource) {
        this.vectorStore = vectorStore;
        this.textResource = pdfResource;
    }

    @PostConstruct
    public void init() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(textResource);
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        vectorStore.add(tokenTextSplitter.apply(tikaDocumentReader.get()));
    }
}
