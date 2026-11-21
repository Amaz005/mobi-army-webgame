package com.mobiarmy.data;

import java.util.ArrayList;
import java.util.List;

public class MissionData {
    public static class MissionGroup {
        public int idNeed;
        public List<MissionEntry> entries = new ArrayList<>();
    }
    public static class MissionEntry {
        public byte index;
        public byte level;
        public String name;
        public String reward;
        public int require;
    }
    public static List<MissionGroup> entries = new ArrayList<>();
}
