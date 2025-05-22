package task.management.test.interview.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import task.management.test.interview.exception.ExternalServiceException;

@Service
@Slf4j
public class ExternalService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Retry(name = "externalApi", fallbackMethod = "fallbackFetch")
    public String fetchData(String url) {
        log.info("Calling external service: {}", url);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ExternalServiceException("External service error: " + ex.getStatusCode());
        } catch (ResourceAccessException ex) {
            throw new ExternalServiceException("Network error while contacting external service");
        } catch (Exception ex) {
            throw new ExternalServiceException("Unexpected error: " + ex.getMessage());
        }
    }

    public String fallbackFetch(String url, Exception ex) {
        log.warn("Fallback called for url: {}, due to: {}", url, ex.getMessage());
        return "Fallback response: external service unavailable";
    }
}