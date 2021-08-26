package com.kvendingoldo.y2p;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;


public class Converter {
    public static void main(String[] args) throws IOException {
        String path = "/Users/asharov/projects/---/poc/toolbox/----kv-manager/versions.yaml";

        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            TreeMap<String, Map<String, Object>> config = new Yaml().loadAs(in, TreeMap.class);
            toProperties(config);
        }
    }

    public static Properties transform(String path) throws IOException {
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            TreeMap<String, Map<String, Object>> config = new Yaml().loadAs(in, TreeMap.class);
            return toProperties(config);
        }
    }

    private static Properties toProperties(TreeMap<String, Map<String, Object>> config) {
        Properties result = new Properties();

        for (String key : config.keySet()) {
            result = transform(result, key, config.get(key));
        }

        return result;
    }

    private static Properties transform(Properties props, String key, Object mapr) {
        if (!(mapr instanceof Map)) {
            props.setProperty(key, mapr.toString());
            return props;
        }

        Map<String, Object> map = (Map<String, Object>) mapr;

        for (String mapKey : map.keySet()) {
            if (map.get(mapKey) instanceof Map) {
                transform(props, key + "." + mapKey, map.get(mapKey));
            } else {
                props.setProperty(String.format("%s.%s", key, mapKey), map.get(mapKey).toString());
            }
        }

        return props;
    }
}