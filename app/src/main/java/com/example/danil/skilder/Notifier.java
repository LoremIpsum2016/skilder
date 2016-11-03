package com.example.danil.skilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by danil on 02.11.16.
 */
public class Notifier {
    private final static Notifier ourInstance = new Notifier();
    private Map<String,SubscriberInfo>  subscribersInfo   = new HashMap<>();

    public interface Subscriber{
        void onNotifyChanged(String message);
        void setSubscriberId(String id);
    }
    private class SubscriberInfo{
        private Subscriber  subscriber;
        private Set<String> subscriptions;
        private boolean     uiRun;

        public SubscriberInfo(Subscriber subscriber, String[] subscriptions, boolean uiRun  ){
            this.subscriber    = subscriber;
            this.subscriptions = new HashSet<>();
            this.uiRun = uiRun;
            Collections.addAll(this.subscriptions, subscriptions);
        }
        public boolean hasSubscription(String subscription){
            return subscription.contains(subscription);
        }
        public void notifySubscriber(final String message){
            if(uiRun){
                Ui.run(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onNotifyChanged(message);
                    }
                });
            } else {
                subscriber.onNotifyChanged(message);
            }
        }

    }

    private Notifier(){
    }

    public static Notifier getInstance(){
        return ourInstance;
    }

    public void subscribe (Subscriber subscriber, String[] subscriptions, boolean uiRun){
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (subscribersInfo.containsKey(id));
        subscriber.setSubscriberId(id);
        subscribersInfo.put(id,new SubscriberInfo(subscriber, subscriptions, uiRun));
    }
    public void subscribe(Subscriber subscriber, String[] subscriptions){
        subscribe(subscriber,subscriptions,true);
    }
    public void unsubscribe(String id){
        subscribersInfo.remove(id);
    }
    private void notifySubscribers(String message){
        for(Map.Entry<String,SubscriberInfo> entry : subscribersInfo.entrySet()){
            SubscriberInfo info = entry.getValue();
            if(info.hasSubscription(message)){
                info.notifySubscriber(message);
            }
        }
    }
    public void publish(String message){
        notifySubscribers(message);
    }

}
