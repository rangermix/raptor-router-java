package io.rangermix.routing.model;

import io.rangermix.routing.enums.RouteType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Route implements Serializable {
    @Serial
    private static final long serialVersionUID = 5278546465986360990L;
    @NotNull
    String id;
    @NotNull Agency agency;
    @NotNull RouteType type;
}
