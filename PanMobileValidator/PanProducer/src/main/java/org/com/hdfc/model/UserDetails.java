package org.com.hdfc.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApplicationScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="UserDetails")
public class UserDetails extends PanacheEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "user_details_seq")
    @SequenceGenerator(name="user_details_seq", sequenceName = "user_details_seq", allocationSize = 1)
    private Long id;

    private String firstName;

    private String lastName;

    private Integer mobileNumber;

    private String panNumber;

    private String address;

    private String gender;

    private Boolean panValidationStatus;

    private Boolean mobileValidationStatus;

    public Uni<Long> save(PgPool client) {
        return client.preparedQuery("INSERT INTO UserDetails(id, firstName, lastName, mobileNumber, panNumber, address, gender) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }
}
