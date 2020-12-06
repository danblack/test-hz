package io.github.danblack.hz;

import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Application {

  public static void main(String[] args) throws InterruptedException {
    HazelcastInstance hz = Hazelcast.newHazelcastInstance();
    hz.getConfig().getMapConfig("customers").setTimeToLiveSeconds(5);
    getDistObjectListener(hz);
    hz.getLifecycleService().addLifecycleListener(Application::log);
    IMap<String, Customer> mapCustomers = hz.getMap("customers");
    addEntryListener(mapCustomers);
    mapCustomers.put("1", new Customer("Customer-1"));
    mapCustomers.put("2", new Customer("Customer-2"));
    mapCustomers.put("3", new Customer("Customer-3"));
    //noinspection InfiniteLoopStatement
    while (true) {
      TimeUnit.SECONDS.sleep(3);
      log("Customer-3: " + mapCustomers.get("3"));
    }
  }

  private static void getDistObjectListener(HazelcastInstance hz) {
    hz.addDistributedObjectListener(
        new DistributedObjectListener() {
          @Override
          public void distributedObjectCreated(DistributedObjectEvent event) {
            log(event);
          }

          @Override
          public void distributedObjectDestroyed(DistributedObjectEvent event) {
            log(event);
          }
        });
  }

  private static void log(Object event) {
    System.out.println("[" + Instant.now() + "] " + event);
  }

  private static void addEntryListener(IMap<String, Customer> mapCustomers) {
    mapCustomers.addEntryListener(
        new EntryListener<String, Customer>() {
          @Override
          public void entryExpired(EntryEvent<String, Customer> event) {
            log(event);
          }

          @Override
          public void entryRemoved(EntryEvent<String, Customer> event) {
            log(event);
          }

          @Override
          public void entryUpdated(EntryEvent<String, Customer> event) {
            log(event);
          }

          @Override
          public void mapCleared(MapEvent event) {
            log(event);
          }

          @Override
          public void mapEvicted(MapEvent event) {
            log(event);
          }

          @Override
          public void entryAdded(EntryEvent event) {
            log(event);
          }

          @Override
          public void entryEvicted(EntryEvent event) {
            log(event);
          }
        },
        true);
  }
}
