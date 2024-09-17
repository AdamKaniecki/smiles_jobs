package pl.zajavka.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.zajavka.infrastructure.cvApi.ApiClient;
import pl.zajavka.infrastructure.cvApi.api.CvRestControllerApi;
import pl.zajavka.infrastructure.cvApi.model.CvDTO;
import pl.zajavka.infrastructure.cvApi.model.CvDTOs;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

//    dla każdego jednego API jest skonfigurowany oddzielnie jeden WebClient, gdyby nasza aplikacja komunikowała się z
//    dwiema aplikacjami to musielibyśmy napisać dwie konfiguracje WebClienta

//    public static final String PET_STORE_URL = "https://swagger-ui/index.html";
    @Value("${api.cv.url}")
    private String cvUrl;
    public static final String API_URL = "http://localhost:9000/swagger-ui/index.html";
    public static final int TIMEOUT = 5000; // api powinno odpowiedzieć w ciągu 5 sekund a jak nie to powinno zwrocic wyjątek
                                            // timeout Exception

    @Bean //WebClient będzie dopisany do Springa jako bean i będzie zastępował curla czyli będzie wywoływał endpointy
    public WebClient webClient(final ObjectMapper objectMapper) {
        final var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));

//   zamiana zarejestrowanego beana Object Mapper na format obiektów json i odwrotnie
        final var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(
                                            objectMapper, MediaType.APPLICATION_JSON
                                    )
                            );
                    configurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(
                                            objectMapper, MediaType.APPLICATION_JSON
                                    )
                            );

                }).build();

//        tworzenie instancji tego Web Clienta
        return WebClient.builder()
                .baseUrl(API_URL)
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

    }
    @Bean
    public ApiClient apiClient(final WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(cvUrl);
        return apiClient;
    }

    @Bean
    public CvRestControllerApi cvRestControllerApi(final ApiClient apiClient) {
        return new CvRestControllerApi(apiClient);
    }


}
