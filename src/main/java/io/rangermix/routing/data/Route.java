package io.rangermix.routing.data;

import io.rangermix.routing.enums.RouteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Route {
    @NotNull String id;
    @NotNull Agency agency;
    @NotNull RouteType type;
}
