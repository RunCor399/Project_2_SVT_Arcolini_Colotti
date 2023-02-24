package it.polito.vulnapp.web.rest;

import it.polito.vulnapp.domain.Example;
import it.polito.vulnapp.repository.ExampleRepository;
import it.polito.vulnapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * REST controller for managing {@link it.polito.vulnapp.domain.Example}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExampleResource {

    private final Logger log = LoggerFactory.getLogger(ExampleResource.class);

    private static final String ENTITY_NAME = "example";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExampleRepository exampleRepository;

    public ExampleResource(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    /**
     * {@code POST  /examples} : Create a new example.
     *
     * @param example the example to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new example, or with status {@code 400 (Bad Request)} if the example has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/examples")
    public Mono<ResponseEntity<Example>> createExample(@RequestBody Example example) throws URISyntaxException {
        log.debug("REST request to save Example : {}", example);
        if (example.getId() != null) {
            throw new BadRequestAlertException("A new example cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return exampleRepository
            .save(example)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/examples/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /examples/:id} : Updates an existing example.
     *
     * @param id the id of the example to save.
     * @param example the example to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated example,
     * or with status {@code 400 (Bad Request)} if the example is not valid,
     * or with status {@code 500 (Internal Server Error)} if the example couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/examples/{id}")
    public Mono<ResponseEntity<Example>> updateExample(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Example example
    ) throws URISyntaxException {
        log.debug("REST request to update Example : {}, {}", id, example);
        if (example.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, example.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return exampleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return exampleRepository
                    .save(example)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /examples/:id} : Partial updates given fields of an existing example, field will ignore if it is null
     *
     * @param id the id of the example to save.
     * @param example the example to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated example,
     * or with status {@code 400 (Bad Request)} if the example is not valid,
     * or with status {@code 404 (Not Found)} if the example is not found,
     * or with status {@code 500 (Internal Server Error)} if the example couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/examples/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Example>> partialUpdateExample(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Example example
    ) throws URISyntaxException {
        log.debug("REST request to partial update Example partially : {}, {}", id, example);
        if (example.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, example.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return exampleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Example> result = exampleRepository
                    .findById(example.getId())
                    .map(existingExample -> {
                        if (example.getName() != null) {
                            existingExample.setName(example.getName());
                        }

                        return existingExample;
                    })
                    .flatMap(exampleRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /examples} : get all the examples.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examples in body.
     */
    @GetMapping("/all-examples")
    public Mono<List<Example>> getAllExamples() {
        log.debug("REST request to get all Examples");
        return exampleRepository.findAll().collectList();
    }

    /**
     * {@code GET  /examples} : get all the examples with the specified name.
     * {@param name} : the filter used as Criteria
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examples in body.
     */
    @GetMapping("/examples")
    public Mono<List<Example>> getAllExamples(@RequestParam("name") String name) {
        log.debug("REST request to get all Examples");
        final Criteria criteria = where("name").is(name);
        return exampleRepository.findAllBy(null, criteria).collectList();
    }

    /**
     * {@code GET  /examples} : get all the examples as a stream.
     * @return the {@link Flux} of examples.
     */
    @GetMapping(value = "/examples", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Example> getAllExamplesAsStream() {
        log.debug("REST request to get all Examples as a stream");
        return exampleRepository.findAll();
    }

    /**
     * {@code GET  /examples/:id} : get the "id" example.
     *
     * @param id the id of the example to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the example, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/examples/{id}")
    public Mono<ResponseEntity<Example>> getExample(@PathVariable String id) {
        log.debug("REST request to get Example : {}", id);
        Mono<Example> example = exampleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(example);
    }

    /**
     * {@code DELETE  /examples/:id} : delete the "id" example.
     *
     * @param id the id of the example to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/examples/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteExample(@PathVariable String id) {
        log.debug("REST request to delete Example : {}", id);
        return exampleRepository
            .deleteById(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
