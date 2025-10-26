package org.example.java.xmlbatch;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final CpeRepo repo;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(CpeRepo repo, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.repo = repo;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public ItemReader<CpeEntity> reader(){
        StaxEventItemReader<CpeEntity> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("cpe_dictionary_sample.xml"));
        reader.setFragmentRootElementName("cpe_item");
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(CpeEntity.class);
        reader.setUnmarshaller(marshaller);
        return reader;
    }

    @Bean
    public ItemProcessor<CpeEntity,CpeEntity> processor(){
        return cpe -> {
            if (cpe.getTitle() == null || cpe.getTitle().isBlank()) return null;
            return cpe;
        };
    }

    @Bean
    public ItemWriter<CpeEntity> writer(){
        return repo::saveAll;
    }

    @Bean
    public Job job(){
        Step step = new StepBuilder("importCSV", jobRepository)
                .<CpeEntity,CpeEntity>chunk(50,transactionManager)
                .reader(reader())
                .writer(writer())
                .processor(processor())
                .build();
        return new JobBuilder("importCSV",jobRepository)
                .start(step)
                .build();
    }
}
