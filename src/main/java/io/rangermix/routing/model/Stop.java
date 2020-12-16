package io.rangermix.routing.model;

import io.rangermix.routing.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Stop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1212227387820663935L;
    final Agency agency;
    final String id;
    final Coordinate coordinate;
    final LocationType locationType;
    Stop parentStation;
    List<Transfer> transfers = new ArrayList<>();
}
