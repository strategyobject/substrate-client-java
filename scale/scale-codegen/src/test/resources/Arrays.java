package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;

import java.util.List;
import java.util.Optional;

@ScaleReader
@ScaleWriter
public class Arrays<T> {
    private String[] stringArray;

    private Optional<Boolean>[] optionalArray;

    private List<T>[] listArray;

    private T[] genericArray;

    @ScaleGeneric(
            template = "Option[][]<I32[]>",
            types = {
                    @Scale(ScaleType.Option[][].class),
                    @Scale(ScaleType.I32[].class)
            })
    private Optional<T>[][] optionalGenericArray;

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public Optional<Boolean>[] getOptionalArray() {
        return optionalArray;
    }

    public void setOptionalArray(Optional<Boolean>[] optionalArray) {
        this.optionalArray = optionalArray;
    }

    public List<T>[] getListArray() {
        return listArray;
    }

    public void setListArray(List<T>[] listArray) {
        this.listArray = listArray;
    }

    public T[] getGenericArray() {
        return genericArray;
    }

    public void setGenericArray(T[] genericArray) {
        this.genericArray = genericArray;
    }

    public Optional<T>[][] getOptionalGenericArray() {
        return optionalGenericArray;
    }

    public void setOptionalGenericArray(Optional<T>[][] optionalGenericArray) {
        this.optionalGenericArray = optionalGenericArray;
    }
}
