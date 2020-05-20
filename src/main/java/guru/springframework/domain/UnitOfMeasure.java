package guru.springframework.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Document
public class UnitOfMeasure {
    @Id
    private String id;
    private String description;

    public UnitOfMeasure(String description) {
        this.description = description;
    }
}
