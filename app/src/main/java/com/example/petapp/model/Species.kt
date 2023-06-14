package com.example.petapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.petapp.R

enum class PetGender {
    MALE,
    FEMALE
}

enum class DimensionUnit(@StringRes override val nameId: Int) : Unit{
    CENTIMETERS(R.string.util_unit_dimension_centimeter),
    METERS(R.string.util_unit_dimension_meters),
    FOOTS(R.string.util_unit_dimension_foot),
    INCHES(R.string.util_unit_dimension_inch)
}

enum class WeightUnit(@StringRes override val nameId: Int) : Unit {
    MILLIGRAMS(R.string.util_unit_weight_mg),
    GRAMS(R.string.util_unit_weight_g),
    KILOGRAMS(R.string.util_unit_weight_kg),
    POUNDS(R.string.util_unit_weight_pounds),
    OUNCE(R.string.util_unit_weight_ounce),
    STONE(R.string.util_unit_weight_stone)
}
enum class Species (@StringRes override val nameId: Int, @DrawableRes val iconId: Int?, val breeds: List<Breed>) : Menu {
    DOG (nameId = R.string.util_enums_species_dog, iconId = null, breeds = DogBreed.values().toList()),
    CAT (nameId = R.string.util_enums_species_cat, iconId = null, breeds = CatBreed.values().toList()),
    FISH (nameId = R.string.util_enums_species_fish, iconId = null, breeds = emptyList()),
    BIRD (nameId = R.string.util_enums_species_bird, iconId = null, breeds = emptyList()),
    REPTILE (nameId = R.string.util_enums_species_reptile, iconId = null, breeds = emptyList()),
    RABBIT (nameId = R.string.util_enums_species_rabbit, iconId = null, breeds = emptyList()),
    HAMSTER (nameId = R.string.util_enums_species_hamster, iconId = null, breeds = emptyList()),
    GUINEA_PIG (nameId = R.string.util_enums_species_guinea_pig, iconId = null, breeds = emptyList()),
    FERRET (nameId = R.string.util_enums_species_ferret, iconId = null, breeds = emptyList()),
    NONE(nameId = R.string.util_blank, iconId = null, breeds = emptyList())
}
interface Menu { val ordinal: Int; val nameId: Int }
interface Breed : Menu { override val nameId: Int; override val ordinal: Int }

interface Unit { val nameId: Int; val ordinal: Int }

enum class DogBreed (@StringRes override val nameId: Int) : Breed {
    LABRADOR_RETRIEVER (R.string.pet_breed_dog_labrador_retriever),
    GERMAN_SHEPHERD_DOG(R.string.pet_breed_dog_german_shepherd_dog),
    POODLE (R.string.pet_breed_dog_poodle),
    CHIHUAHUA(R.string.pet_breed_dog_chihuahua),
    GOLDEN_RETRIEVER(R.string.pet_breed_dog_golden_retriever),
    YORKSHIRE_TERRIER(R.string.pet_breed_dog_yorkshire_terrier),
    DACHSHUND(R.string.pet_breed_dog_dachshund),
    BEAGLE(R.string.pet_breed_dog_beagle),
    BOXER(R.string.pet_breed_dog_boxer),
    MINIATURE_SCHNAUZER(R.string.pet_breed_dog_miniature_schnauzer),
    SHI_TZU(R.string.pet_breed_shi_tzu),
    BULlDOG(R.string.pet_breed_dog_bulldog),
    GERMAN_SPITZ(R.string.pet_breed_german_spitz),
    ENGLISH_COCKER_SPANIEL(R.string.pet_breed_dog_english_cocker_spaniel),
    CAVALIER_KING_CHARLES_SPANIEL(R.string.pet_breed_dog_cavalier_king_charles_spaniel),
    FRENCH_BULLDOG(R.string.pet_breed_dog_french_bulldog),
    PUG(R.string.pet_breed_dog_pug),
    ROTTWEILER(R.string.pet_breed_dog_rottweiler),
    ENGLISH_SETTER(R.string.pet_breed_dog_english_setter),
    MALTESE(R.string.pet_breed_dog_maltese),
    ENGLISH_SPRINGER_SPANIEL(R.string.pet_breed_dog_english_springer_spaniel),
    GERMAN_SHORTHAIRED_POINTER(R.string.pet_breed_dog_german_shorthaired_pointer),
    STAFFORDSHIRE_BULL_TERRIER(R.string.pet_breed_dog_staffordshire_bull_terrier),
    BORDER_COLLIE(R.string.pet_breed_dog_border_collie),
    SHETLAND_SHEEPDOG(R.string.pet_breed_dog_shetland_sheepdog),
    DOBERMANN(R.string.pet_breed_dog_dobermann),
    WEST_HIGHLAND_WHITE_TERRIER(R.string.pet_breed_dog_west_highland_white_terrier),
    BERNESE_MOUNTAIN(R.string.pet_breed_dog_bernese_mountain),
    GREAT_DANE(R.string.pet_breed_dog_great_dane),
    BRITTANY_SPANIEL(R.string.pet_breed_dog_brittany_spaniel),
    NONE(R.string.util_blank)
}

enum class CatBreed (@StringRes override val nameId: Int) : Breed {
    ABYSSINIAN(R.string.pet_breed_cat_abyssinian),
    AMERICAN_BOBTAIL(R.string.pet_breed_cat_american_bobtail),
    AMERICAN_SHORTHAIR(R.string.pet_breed_cat_american_shorthair),
    BENGAL (R.string.pet_breed_cat_bengal),
    BIRMAN (R.string.pet_breed_cat_birman),
    BOMBAY (R.string.pet_breed_cat_bombay),
    BRITISH_SHORTHAIR (R.string.pet_breed_cat_british_shorthair),
    DEVON_REX (R.string.pet_breed_cat_devon_rex),
    DOMESTIC_LONGHAIR (R.string.pet_breed_cat_domestic_longhair),
    EXOTIC_SHORTHAIR (R.string.pet_breed_cat_american_shorthair),
    HIMALAYAN (R.string.pet_breed_cat_himalayan),
    MAINE_COON (R.string.pet_breed_cat_maine_coon),
    NORWEGIAN_FOREST (R.string.pet_breed_cat_norwegian_forest),
    PERSIAN (R.string.pet_breed_cat_persian),
    RAGDOLL (R.string.pet_breed_cat_ragdoll),
    SAVANNAH (R.string.pet_breed_cat_savannah),
    SCOTTISH_FOLD (R.string.pet_breed_cat_scottish_fold),
    SIAMESE (R.string.pet_breed_cat_siamese),
    SPHYNX (R.string.pet_breed_cat_sphynx),
    NONE(R.string.util_blank)
}