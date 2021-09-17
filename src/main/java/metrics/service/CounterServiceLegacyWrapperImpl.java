package metrics.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterServiceLegacyWrapperImpl implements CounterService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void increment(String name) {
        meterRegistry.counter(name, Tags.empty()).increment();
    }
}
