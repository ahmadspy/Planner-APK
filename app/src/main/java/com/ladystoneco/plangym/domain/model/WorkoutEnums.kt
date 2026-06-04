package com.ladystoneco.plangym.domain.model

enum class ExerciseCategory {
    STRENGTH, CARDIO, MOBILITY, STRETCHING, BODYWEIGHT, CUSTOM
}

enum class MuscleGroup {
    CHEST, BACK, SHOULDERS, BICEPS, TRICEPS, LEGS, GLUTES, CORE, FULL_BODY, CARDIO, OTHER
}

enum class EquipmentType {
    NONE, DUMBBELL, BARBELL, MACHINE, CABLE, KETTLEBELL, RESISTANCE_BAND, TREADMILL, BIKE, OTHER
}

enum class WorkoutSessionStatus {
    ACTIVE, COMPLETED, CANCELLED
}
