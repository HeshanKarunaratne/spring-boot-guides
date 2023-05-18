package com.example.springbatch1.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Heshan Karunaratne
 */
@RestController
@RequestMapping("/api/job")
@AllArgsConstructor
public class JobController {

    private JobLauncher jobLauncher;
    private Job job;

    private final String TEMP_STORAGE = "C:/Users/hkarunaratne/Desktop/Docs/batch-files/";

    @PostMapping("/import")
    public void importCsvToDbJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/import/v2")
    public void startBatch(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        String originalFileName = multipartFile.getOriginalFilename();
        File fileToImport = new File(TEMP_STORAGE + originalFileName);
        multipartFile.transferTo(fileToImport);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", TEMP_STORAGE + originalFileName)
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution execution = jobLauncher.run(job, jobParameters);

            if (execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED)) {
                // Delete the file from the TEMP_STORAGE
                Files.deleteIfExists(Paths.get(TEMP_STORAGE + originalFileName));
            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
