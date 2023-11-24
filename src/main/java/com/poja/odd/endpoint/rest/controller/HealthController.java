package com.poja.odd.endpoint.rest.controller;

import static java.util.UUID.randomUUID;

import com.poja.odd.PojaGenerated;
import com.poja.odd.endpoint.event.EventProducer;
import com.poja.odd.endpoint.event.gen.UuidCreated;
import com.poja.odd.repository.DummyRepository;
import com.poja.odd.repository.DummyUuidRepository;
import com.poja.odd.repository.model.Dummy;
import com.poja.odd.repository.model.DummyUuid;
import java.util.List;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@Value
public class HealthController {

  DummyRepository dummyRepository;
  DummyUuidRepository dummyUuidRepository;
  EventProducer eventProducer;

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/dummy-table")
  public List<Dummy> dummyTable() {
    return dummyRepository.findAll();
  }

  @GetMapping(value = "/uuid-created")
  public String uuidCreated() throws InterruptedException {
    var randomUuid = randomUUID().toString();
    var event = new UuidCreated().toBuilder().uuid(randomUuid).build();

    eventProducer.accept(List.of(event));

    Thread.sleep(20_000);
    return dummyUuidRepository.findById(randomUuid).map(DummyUuid::getId).orElseThrow();
  }

  @GetMapping("/odd")
  public int getOdd() {
    int n = (int) (Integer.MAX_VALUE * Math.random());
    return n%2 == 0 ? n-1 : n;
  }

}
