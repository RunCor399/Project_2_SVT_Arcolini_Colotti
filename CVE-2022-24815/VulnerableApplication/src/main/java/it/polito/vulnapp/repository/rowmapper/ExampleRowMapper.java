package it.polito.vulnapp.repository.rowmapper;

import io.r2dbc.spi.Row;
import it.polito.vulnapp.domain.Example;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Example}, with proper type conversions.
 */
@Service
public class ExampleRowMapper implements BiFunction<Row, String, Example> {

    private final ColumnConverter converter;

    public ExampleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Example} stored in the database.
     */
    @Override
    public Example apply(Row row, String prefix) {
        Example entity = new Example();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
