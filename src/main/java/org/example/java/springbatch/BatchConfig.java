package org.example.java.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RecipeRepo recipeRepo;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, RecipeRepo recipeRepo) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.recipeRepo = recipeRepo;
    }

    @Bean
    public ItemReader<RecipeEntity> recipeReader() {
        return new JsonItemReaderBuilder<RecipeEntity>()
                .name("recipeReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(RecipeEntity.class))
                .resource(new ClassPathResource("Recipe.json"))
                .build();
    }

    @Bean
    public ItemProcessor<RecipeEntity, RecipeEntity> recipeProcessor() {
        return recipe -> {
            if (recipe.getRating() != null && Double.isNaN(recipe.getRating())) recipe.setRating(null);
            if (recipe.getPrepTime() != null && recipe.getPrepTime() < 0) recipe.setPrepTime(null);
            if (recipe.getCookTime() != null && recipe.getCookTime() < 0) recipe.setCookTime(null);
            if (recipe.getTotalTime() != null && recipe.getTotalTime() < 0) recipe.setTotalTime(null);
            return recipe;
        };

    }

    @Bean
    public ItemWriter<RecipeEntity> recipeWriter() {
        RepositoryItemWriter<RecipeEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(recipeRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importRecipeJob(){
        Step step = new StepBuilder("importRecipeStep",jobRepository)
                .<RecipeEntity,RecipeEntity>chunk(10,transactionManager)
                .reader(recipeReader())
                .processor(recipeProcessor())
                .writer(recipeWriter())
                .build();
        return new JobBuilder("importRecipeJob",jobRepository)
                .start(step)
                .build();
    }
}