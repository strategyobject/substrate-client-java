use jni::{
    JNIEnv,
    objects::JClass,
    sys::{jboolean, jbyteArray},
};

use crate::sr25519::*;
use std::panic::catch_unwind;
use std::ops::Deref;
use std::any::Any;

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_deriveKeyPairHard(
    env: JNIEnv,
    _class: JClass,
    key_pair: jbyteArray,
    chain_code: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let key_pair = env.convert_byte_array(key_pair).expect("`key_pair` is not set.");
        let chain_code = env.convert_byte_array(chain_code).expect("`chain_code` is not set.");

        ext_sr_derive_keypair_hard(&key_pair, &chain_code)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_deriveKeyPairSoft(
    env: JNIEnv,
    _class: JClass,
    key_pair: jbyteArray,
    chain_code: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let key_pair = env.convert_byte_array(key_pair).expect("`key_pair` is not set.");
        let chain_code = env.convert_byte_array(chain_code).expect("`chain_code` is not set.");

        ext_sr_derive_keypair_soft(&key_pair, &chain_code)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_derivePublicSoft(
    env: JNIEnv,
    _class: JClass,
    pub_key: jbyteArray,
    chain_code: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let pub_key = env.convert_byte_array(pub_key).expect("`pub_key` is not set.");
        let chain_code = env.convert_byte_array(chain_code).expect("`chain_code` is not set.");

        ext_sr_derive_public_soft(&pub_key, &chain_code)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_fromSeed(
    env: JNIEnv,
    _class: JClass,
    seed: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let seed = env.convert_byte_array(seed).expect("`seed` is not set.");

        ext_sr_from_seed(&seed)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_fromPair(
    env: JNIEnv,
    _class: JClass,
    pair: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let pair = env.convert_byte_array(pair).expect("`pair` is not set.");

        ext_sr_from_pair(&pair)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_sign(
    env: JNIEnv,
    _class: JClass,
    pub_key: jbyteArray,
    sec: jbyteArray,
    msg: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let pub_key = env.convert_byte_array(pub_key).expect("`pub_key` is not set.");
        let sec = env.convert_byte_array(sec).expect("`sec` is not set.");
        let msg = env.convert_byte_array(msg).expect("`msg` is not set.");

        ext_sr_sign(&pub_key, &sec, &msg)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_verify(
    env: JNIEnv,
    _class: JClass,
    sig: jbyteArray,
    msg: jbyteArray,
    pub_key: jbyteArray,
) -> jboolean {
    catch_unwind(|| {
        let sig = env.convert_byte_array(sig).expect("`sig` is not set.");
        let msg = env.convert_byte_array(msg).expect("`msg` is not set.");
        let pub_key = env.convert_byte_array(pub_key).expect("`pub_key` is not set.");

        ext_sr_verify(&sig, &msg, &pub_key) as jboolean
    })
        .unwrap_or_else(|e| error_bool(env, e))
}

#[no_mangle]
pub extern "system" fn Java_com_strategyobject_substrateclient_crypto_sr25519_Native_agree(
    env: JNIEnv,
    _class: JClass,
    pub_key: jbyteArray,
    sec: jbyteArray,
) -> jbyteArray {
    catch_unwind(|| {
        let pub_key = env.convert_byte_array(pub_key).expect("`pub_key` is not set.");
        let sec = env.convert_byte_array(sec).expect("`sec` is not set.");

        ext_sr_agree(&pub_key, &sec)
    })
        .map(|r| env.byte_array_from_slice(&r).unwrap())
        .unwrap_or_else(|e| error_array(env, e))
}

fn error_array(env: JNIEnv, e: Box<dyn Any + Send>) -> jbyteArray {
    let err = env.new_byte_array(0).unwrap(); // it must be called before `throw`
    let msg = e.downcast_ref::<String>()
        .map(String::as_str)
        .or_else(|| e.downcast_ref::<&'static str>().map(Deref::deref))
        .unwrap_or("Unknown exception.");

    env.throw_new("com/strategyobject/substrateclient/crypto/NativeException", msg).unwrap();

    err
}

fn error_bool(env: JNIEnv, e: Box<dyn Any + Send>) -> jboolean {
    let err = false as jboolean; // it must be called before `throw`
    let msg = e.downcast_ref::<String>()
        .map(String::as_str)
        .or_else(|| e.downcast_ref::<&'static str>().map(Deref::deref))
        .unwrap_or("Unknown exception.");

    env.throw_new("com/strategyobject/substrateclient/crypto/NativeException", msg).unwrap();

    err
}