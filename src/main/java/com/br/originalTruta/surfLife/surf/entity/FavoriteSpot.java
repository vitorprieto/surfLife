package com.br.originalTruta.surfLife.surf.entity;

import com.br.originalTruta.surfLife.common.entity.BaseAuditableEntity;
import com.br.originalTruta.surfLife.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "favorite_spots",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_favorite_spots_user_spot", columnNames = {"user_id", "spot_id"})
        }
)
public class FavoriteSpot extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
}