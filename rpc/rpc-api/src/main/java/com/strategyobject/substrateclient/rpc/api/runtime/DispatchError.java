package com.strategyobject.substrateclient.rpc.api.runtime;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.union.Union;

/**
 * Reason why a dispatch call failed.
 */
public class DispatchError extends Union {

    private DispatchError() {
    }

    /**
     * @return true if some error occurred
     */
    public boolean isOther() {
        return index == 0;
    }

    /**
     * @return true if failed to lookup some data
     */
    public boolean isCannotLookup() {
        return index == 1;
    }

    /**
     * @return true if is a bad origin
     */
    public boolean isBadOrigin() {
        return index == 2;
    }

    /**
     * @return true if is a custom error in a module
     */
    public boolean isModule() {
        return index == 3;
    }

    /**
     * @return true if at least one consumer is remaining so the account cannot be destroyed
     */
    public boolean isConsumerRemaining() {
        return index == 4;
    }

    /**
     * @return true if there are no providers so the account cannot be created
     */
    public boolean isNoProviders() {
        return index == 5;
    }

    public boolean isTooManyConsumers() { return index == 6; }

    /**
     * @return true if is an error to do with tokens
     */
    public boolean isToken() {
        return index == 7;
    }

    /**
     * @return true if is an arithmetic error
     */
    public boolean isArithmetic() {
        return index == 8;
    }

    public boolean isTransactional() { return index == 9; }

    /**
     * @return module error
     */
    public ModuleError getModuleError() {
        Preconditions.checkState(index == 3);
        return (ModuleError) value;
    }

    /**
     * @return token error
     */
    public TokenError getTokenError() {
        Preconditions.checkState(index == 6);
        return (TokenError) value;
    }

    /**
     * @return arithmetic error
     */
    public ArithmeticError getArithmeticError() {
        Preconditions.checkState(index == 7);
        return (ArithmeticError) value;
    }

    public static DispatchError ofOther() {
        DispatchError result = new DispatchError();
        result.index = 0;
        return result;
    }

    public static DispatchError ofCannotLookup() {
        DispatchError result = new DispatchError();
        result.index = 1;
        return result;
    }

    public static DispatchError ofBadOrigin() {
        DispatchError result = new DispatchError();
        result.index = 2;
        return result;
    }

    public static DispatchError ofModule(ModuleError moduleError) {
        DispatchError result = new DispatchError();
        result.value = moduleError;
        result.index = 3;
        return result;
    }

    public static DispatchError ofConsumerRemaining() {
        DispatchError result = new DispatchError();
        result.index = 4;
        return result;
    }

    public static DispatchError ofNoProviders() {
        DispatchError result = new DispatchError();
        result.index = 5;
        return result;
    }

    public static DispatchError ofTooManyConsumer() {
        DispatchError result = new DispatchError();
        result.index = 6;
        return result;
    }

    public static DispatchError ofToken(TokenError tokenError) {
        DispatchError result = new DispatchError();
        result.value = tokenError;
        result.index = 7;
        return result;
    }

    public static DispatchError ofArithmetic(ArithmeticError arithmeticError) {
        DispatchError result = new DispatchError();
        result.value = arithmeticError;
        result.index = 8;
        return result;
    }

    public static DispatchError ofTransactional(TransactionalError transactionalError) {
        DispatchError result = new DispatchError();
        result.value = transactionalError;
        result.index = 9;
        return result;
    }
}
