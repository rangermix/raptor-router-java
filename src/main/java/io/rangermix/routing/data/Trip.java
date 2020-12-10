package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Trip implements Serializable {
    @Serial
    private static final long serialVersionUID = -860604145187379705L;
    @NotNull Route route;
    @NotNull Service service;
    @NotNull String id;
    List<StopTime> stopTimes;
}
