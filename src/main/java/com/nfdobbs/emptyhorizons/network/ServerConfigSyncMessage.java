package com.nfdobbs.emptyhorizons.network;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class ServerConfigSyncMessage implements IMessage {

    public HashMap<Integer, Float> dimensionMultipliers = new HashMap<>();

    public ServerConfigSyncMessage() {};

    public ServerConfigSyncMessage(HashMap<Integer, Float> serverConfigMap) {
        dimensionMultipliers = serverConfigMap;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int byteLength = buf.readInt();
        ByteBuf bytes = buf.readBytes(byteLength);

        String serializedConfig = new String(bytes.array(), StandardCharsets.UTF_8);

        String[] keyValueStrings = serializedConfig.split(("\\|"));

        for (var keyValuePair : keyValueStrings) {
            String[] splitPair = keyValuePair.split(",");

            // Safety Check
            if (splitPair.length == 2) {
                String key = splitPair[0];
                String value = splitPair[1];

                Integer dimension = null;
                Float multiplier = null;

                try {
                    dimension = Integer.parseInt(key);
                    multiplier = Float.parseFloat(value);
                } catch (NumberFormatException ex) {
                    // We don't do anything if we fail
                }

                if (dimension != null && multiplier != null) {
                    dimensionMultipliers.put(dimension, multiplier);
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        StringBuilder serializedConfig = new StringBuilder();

        for (var entry : dimensionMultipliers.entrySet()) {
            serializedConfig.append(entry.getKey())
                .append(",")
                .append(entry.getValue())
                .append("|");
        }

        byte[] bytes = serializedConfig.toString()
            .getBytes(StandardCharsets.UTF_8);
        int byteLength = bytes.length;

        buf.writeInt(byteLength);
        buf.writeBytes(bytes);
    }
}
