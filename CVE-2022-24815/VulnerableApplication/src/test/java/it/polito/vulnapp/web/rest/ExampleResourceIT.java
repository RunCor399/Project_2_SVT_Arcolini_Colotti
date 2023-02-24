package it.polito.vulnapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import it.polito.vulnapp.IntegrationTest;
import it.polito.vulnapp.domain.Example;
import it.polito.vulnapp.repository.EntityManager;
import it.polito.vulnapp.repository.ExampleRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ExampleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExampleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/examples";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Example example;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Example createEntity(EntityManager em) {
        Example example = new Example().name(DEFAULT_NAME);
        return example;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Example createUpdatedEntity(EntityManager em) {
        Example example = new Example().name(UPDATED_NAME);
        return example;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Example.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        example = createEntity(em);
    }

    @Test
    void createExample() throws Exception {
        int databaseSizeBeforeCreate = exampleRepository.findAll().collectList().block().size();
        // Create the Example
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeCreate + 1);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createExampleWithExistingId() throws Exception {
        // Create the Example with an existing ID
        example.setId("existing_id");

        int databaseSizeBeforeCreate = exampleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExamplesAsStream() {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        List<Example> exampleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Example.class)
            .getResponseBody()
            .filter(example::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(exampleList).isNotNull();
        assertThat(exampleList).hasSize(1);
        Example testExample = exampleList.get(0);
        assertThat(testExample.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllExamples() {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        // Get all the exampleList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(example.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getExample() {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        // Get the example
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, example.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(example.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingExample() {
        // Get the example
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewExample() throws Exception {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();

        // Update the example
        Example updatedExample = exampleRepository.findById(example.getId()).block();
        updatedExample.name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedExample.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedExample))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, example.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExampleWithPatch() throws Exception {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();

        // Update the example using partial update
        Example partialUpdatedExample = new Example();
        partialUpdatedExample.setId(example.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExample.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExample))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateExampleWithPatch() throws Exception {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();

        // Update the example using partial update
        Example partialUpdatedExample = new Example();
        partialUpdatedExample.setId(example.getId());

        partialUpdatedExample.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExample.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExample))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, example.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().collectList().block().size();
        example.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(example))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExample() {
        // Initialize the database
        example.setId(UUID.randomUUID().toString());
        exampleRepository.save(example).block();

        int databaseSizeBeforeDelete = exampleRepository.findAll().collectList().block().size();

        // Delete the example
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, example.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Example> exampleList = exampleRepository.findAll().collectList().block();
        assertThat(exampleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
