package com.hotel.reservation.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID reservationID = UUID.randomUUID();

    @OneToOne(mappedBy = "reservation")
    private Room room;

    private Date reservationDate;


}
