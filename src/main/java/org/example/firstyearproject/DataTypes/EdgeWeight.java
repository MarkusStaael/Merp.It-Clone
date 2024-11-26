package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

public enum EdgeWeight implements Serializable {
    MOTORWAY {
        @Override
        public float getSpeed() { return 130.0f; }
    },
    TRUNK {
        @Override
        public float getSpeed() { return 90.0f; }
    },
    LIVING_STREET {
        @Override
        public float getSpeed() { return 15.0f; }
    },
    N_ARY {
        @Override
        public float getSpeed() { return 80.0f; }
    },
    RESIDENTIAL {
        @Override
        public float getSpeed() { return 50.0f; }
    },
    SERVICE {
        @Override
        public float getSpeed() { return 20.0f; }
    },
    UNCLASSIFIED {
        @Override
        public float getSpeed() { return 80.0f; }
    };

    public abstract float getSpeed();
}