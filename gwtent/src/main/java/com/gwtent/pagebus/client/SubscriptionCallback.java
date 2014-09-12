package com.gwtent.pagebus.client;

public interface SubscriptionCallback {

  void execute(String subject, Object message);
}
