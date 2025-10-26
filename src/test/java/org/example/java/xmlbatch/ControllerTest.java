package org.example.java.xmlbatch;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Service service;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job job;

    @Test
    public void startBatchJobTest() throws Exception{
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        Mockito.when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class)))
                .thenReturn(jobExecution);

        mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/cpes/start")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Batch Job Completed Successfully")));
    }

    @Test
    public void getAllCpeSTest() throws Exception{
        CpeEntity cpe = new CpeEntity();
        cpe.setId(1L);
        cpe.setTitle("Example CPE");
        cpe.setCpe22URI("uri22");
        cpe.setCpe23URI("uri23");
        cpe.setCpe22DeprecationDate(LocalDate.of(2024, 1, 1));
        cpe.setCpe23DeprecationDate(LocalDate.of(2024, 2, 1));

        Page<CpeEntity> page = new PageImpl<>(List.of(cpe));
        Mockito.when(service.getAll(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cpes")
                .param("page","1")
                .param("limit","10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Page").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.limit").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Total").value(page.getTotalElements()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data[0].title").value("Example CPE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data[0].cpe22URI").value("uri22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data[0].cpe23URI").value("uri23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data[0].cpe22DeprecationDate").value("2024-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data[0].cpe23DeprecationDate").value("2024-02-01"));
    }

    @Test
    public void searchCpeSTest() throws Exception{
        CpeEntity cpe = new CpeEntity();
        cpe.setId(1L);
        cpe.setTitle("Example CPE");
        cpe.setCpe22URI("uri22");
        cpe.setCpe23URI("uri23");
        cpe.setCpe22DeprecationDate(LocalDate.of(2024, 1, 1));
        cpe.setCpe23DeprecationDate(LocalDate.of(2024, 2, 1));
        Mockito.when(service.searchCpeS(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(List.of(cpe));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cpes/search")
                .param("cpe_title","Example CPE")
                .param("cpe_22_uri","uri22")
                .param("cpe_23_uri","uri23")
                .param("deprecation_date","Example CPE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Example CPE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cpe22URI").value("uri22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cpe23URI").value("uri23"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cpe22DeprecationDate").value("2024-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].cpe23DeprecationDate").value("2024-02-01"));
    }
}
