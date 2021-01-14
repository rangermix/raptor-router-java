package io.rangermix.raptorrouter.routing;

import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.routing.model.Stop;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

@RequiredArgsConstructor
public class Query {
    final DataPackage dataPackage;
    final Stop from;
    final Stop to;
    final long startTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Query query = (Query) o;
        return startTime == query.startTime && dataPackage.equals(query.dataPackage) && from.equals(query.from) && to.equals(
                query.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataPackage, from, to, startTime);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Query.class.getSimpleName() + "[", "]").add("dataPackage=" + dataPackage)
                .add("from=" + from)
                .add("to=" + to)
                .add("startTime=" + startTime)
                .toString();
    }
}
