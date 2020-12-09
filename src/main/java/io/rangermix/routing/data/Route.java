package io.rangermix.routing.data;

import io.rangermix.routing.enums.BoardingInstruction;
import io.rangermix.routing.enums.RouteType;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@Data
public class Route {
    @NotNull String id;
    @NotNull Agency agency;
    String shortName;
    String longName;
    String description;
    @NotNull
    RouteType type;
    String url;
    Color color = Color.WHITE;
    Color textColor = Color.BLACK;
    int sortOrder = Integer.MAX_VALUE;
    BoardingInstruction continuousPickup = BoardingInstruction.NOT_AVAILABLE;
    BoardingInstruction continuousDropOff = BoardingInstruction.NOT_AVAILABLE;
}
