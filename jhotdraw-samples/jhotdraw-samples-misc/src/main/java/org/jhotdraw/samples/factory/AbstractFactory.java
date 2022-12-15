package org.jhotdraw.samples.factory;

public interface AbstractFactory<T>{
    T create(String type);
}
