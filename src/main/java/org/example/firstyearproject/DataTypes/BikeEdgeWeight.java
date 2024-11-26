package org.example.firstyearproject.DataTypes;

import java.io.Serializable;

public enum BikeEdgeWeight implements Serializable {
    LANE {
        @Override
        public float getSpeed() { return 22.0f; }
    },
    OPPOSITE {
        @Override
        public float getSpeed() { return 20.0f; }
    },
    TRACK {
        @Override
        public float getSpeed() { return 25.0f; }
    },
    SHARE_BUSWAY {
        @Override
        public float getSpeed() { return 17.0f; }
    },
    SHARED_LANE {
        @Override
        public float getSpeed() { return 15.0f; }
    },
    TRUNK {
        @Override
        public float getSpeed() { return 25.0f; }
    },
    LIVING_STREET {
        @Override
        public float getSpeed() { return 18.0f; }
    },
    N_ARY {
        @Override
        public float getSpeed() { return 30.0f; }
    },
    RESIDENTIAL {
        @Override
        public float getSpeed() { return 16.0f; }
    },
    SERVICE {
        @Override
        public float getSpeed() { return 19.0f; }
    },
    UNCLASSIFIED {
        @Override
        public float getSpeed() { return 20.0f; }
    };

    public abstract float getSpeed();
}