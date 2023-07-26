package com.example.petapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.petapp.R

enum class PetGender (@StringRes val nameId: Int, @DrawableRes val iconId: Int) {
    MALE(R.string.util_gender_male, R.drawable.round_male_24),
    FEMALE(R.string.util_gender_female, R.drawable.round_female_24)
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
enum class Species (@StringRes override val nameId: Int,
                    @DrawableRes override val avatarIconId: Int? = null, @DrawableRes val iconId: Int? = null, val breeds: List<Breed>) : Menu {
    DOG (nameId = R.string.util_enums_species_dog, avatarIconId = R.drawable.species_dog_128, breeds = DogBreed.values().toList()),
    CAT (nameId = R.string.util_enums_species_cat, avatarIconId = R.drawable.species_cat_128, breeds = CatBreed.values().toList()),
    FISH (nameId = R.string.util_enums_species_fish, avatarIconId = R.drawable.species_fish_128, breeds = emptyList()),
    BIRD (nameId = R.string.util_enums_species_bird, avatarIconId = R.drawable.species_bird_128, breeds = emptyList()),
    REPTILE (nameId = R.string.util_enums_species_reptile, avatarIconId = R.drawable.species_chameleon_128, breeds = emptyList()),
    AMPHIBIAN(nameId = R.string.util_enums_amphibians, avatarIconId = R.drawable.species_frog_128, breeds = emptyList()),
    MOUSE(nameId = R.string.util_enums_mouse, avatarIconId = R.drawable.species_mouse_128, breeds = emptyList()),
    TURTLE(nameId = R.string.util_enums_turtle, avatarIconId = R.drawable.species_turtle_128, breeds = emptyList()),
    RABBIT (nameId = R.string.util_enums_species_rabbit, avatarIconId = R.drawable.species_rabbit_128, breeds = emptyList()),
    HAMSTER (nameId = R.string.util_enums_species_hamster, avatarIconId = R.drawable.species_hamster_128, breeds = emptyList()),
    GUINEA_PIG (nameId = R.string.util_enums_species_guinea_pig, avatarIconId = R.drawable.species_guinea_pig_128, breeds = emptyList()),
    FERRET (nameId = R.string.util_enums_species_ferret, avatarIconId = R.drawable.species_ferret_128, breeds = emptyList()),
    NONE(nameId = R.string.util_blank, avatarIconId = null, breeds = emptyList())
}
interface Menu {
    val ordinal: Int
    val nameId: Int
    val avatarIconId: Int?
}
interface Breed : Menu {
    override val nameId: Int
    override val ordinal: Int

}

interface Unit {
    val nameId: Int
    val ordinal: Int
}

enum class DogBreed (@StringRes override val nameId: Int,
                     @DrawableRes override val avatarIconId: Int?
                     ) : Breed {
    LABRADOR_RETRIEVER (R.string.pet_breed_dog_labrador_retriever, null),
    GERMAN_SHEPHERD_DOG(R.string.pet_breed_dog_german_shepherd_dog, R.drawable.german_shepherd),
    POODLE (R.string.pet_breed_dog_poodle, R.drawable.poodle),
    CHIHUAHUA(R.string.pet_breed_dog_chihuahua, R.drawable.chihuahua),
    GOLDEN_RETRIEVER(R.string.pet_breed_dog_golden_retriever, null),
    YORKSHIRE_TERRIER(R.string.pet_breed_dog_yorkshire_terrier, R.drawable.yorkshire_terrier),
    DACHSHUND(R.string.pet_breed_dog_dachshund, R.drawable.dachshund),
    BEAGLE(R.string.pet_breed_dog_beagle, R.drawable.beagle),
    BOXER(R.string.pet_breed_dog_boxer, R.drawable.boxer),
    MINIATURE_SCHNAUZER(R.string.pet_breed_dog_miniature_schnauzer, R.drawable.miniature_schnauzer),
    SHI_TZU(R.string.pet_breed_shi_tzu, R.drawable.shih_tzu),
    BULlDOG(R.string.pet_breed_dog_bulldog, R.drawable.bulldog),
    GERMAN_SPITZ(R.string.pet_breed_german_spitz, null),
    ENGLISH_COCKER_SPANIEL(R.string.pet_breed_dog_english_cocker_spaniel, R.drawable.cocker_spaniel),
    CAVALIER_KING_CHARLES_SPANIEL(R.string.pet_breed_dog_cavalier_king_charles_spaniel, R.drawable.cocker_spaniel),
    FRENCH_BULLDOG(R.string.pet_breed_dog_french_bulldog, R.drawable.french_bulldog),
    PUG(R.string.pet_breed_dog_pug, R.drawable.pug),
    ROTTWEILER(R.string.pet_breed_dog_rottweiler, R.drawable.rottweiler),
    ENGLISH_SETTER(R.string.pet_breed_dog_english_setter, null),
    MALTESE(R.string.pet_breed_dog_maltese, null),
    ENGLISH_SPRINGER_SPANIEL(R.string.pet_breed_dog_english_springer_spaniel, R.drawable.springer_spaniel),
    GERMAN_SHORTHAIRED_POINTER(R.string.pet_breed_dog_german_shorthaired_pointer, null),
    STAFFORDSHIRE_BULL_TERRIER(R.string.pet_breed_dog_staffordshire_bull_terrier, R.drawable.bull_terrier),
    BORDER_COLLIE(R.string.pet_breed_dog_border_collie, R.drawable.border_collie),
    SHETLAND_SHEEPDOG(R.string.pet_breed_dog_shetland_sheepdog, R.drawable.shetland_sheepdog),
    DOBERMANN(R.string.pet_breed_dog_dobermann, R.drawable.doberman),
    WEST_HIGHLAND_WHITE_TERRIER(R.string.pet_breed_dog_west_highland_white_terrier, null),
    BERNESE_MOUNTAIN(R.string.pet_breed_dog_bernese_mountain, R.drawable.bernese_mountain),
    GREAT_DANE(R.string.pet_breed_dog_great_dane, null),
    BRITTANY_SPANIEL(R.string.pet_breed_dog_brittany_spaniel, null),
    NONE(R.string.util_blank, null)
}

enum class CatBreed (@StringRes override val nameId: Int,
                     @DrawableRes override val avatarIconId: Int?) : Breed {
    ABYSSINIAN(R.string.pet_breed_cat_abyssinian, R.drawable.abyssinian_cat),
    AMERICAN_BOBTAIL(R.string.pet_breed_cat_american_bobtail, R.drawable.japanese_bobtail),
    AMERICAN_SHORTHAIR(R.string.pet_breed_cat_american_shorthair, null),
    BENGAL (R.string.pet_breed_cat_bengal, R.drawable.bengal),
    BIRMAN (R.string.pet_breed_cat_birman, null),
    BOMBAY (R.string.pet_breed_cat_bombay, R.drawable.bombay),
    BRITISH_SHORTHAIR (R.string.pet_breed_cat_british_shorthair, R.drawable.british_shorthair),
    DEVON_REX (R.string.pet_breed_cat_devon_rex, R.drawable.selkirk_rex_cat),
    DOMESTIC_LONGHAIR (R.string.pet_breed_cat_domestic_longhair, null),
    EXOTIC_SHORTHAIR (R.string.pet_breed_cat_american_shorthair, R.drawable.exotic_shorthair),
    HIMALAYAN (R.string.pet_breed_cat_himalayan, R.drawable.himalayan_cat),
    MAINE_COON (R.string.pet_breed_cat_maine_coon, R.drawable.maine_coon),
    NORWEGIAN_FOREST (R.string.pet_breed_cat_norwegian_forest, R.drawable.norwegian_forest_cat2),
    PERSIAN (R.string.pet_breed_cat_persian, R.drawable.persian),
    RAGDOLL (R.string.pet_breed_cat_ragdoll, R.drawable.black_ragdoll),
    SAVANNAH (R.string.pet_breed_cat_savannah, R.drawable.savannah_cat),
    SCOTTISH_FOLD (R.string.pet_breed_cat_scottish_fold, R.drawable.scottish_fold),
    SIAMESE (R.string.pet_breed_cat_siamese, R.drawable.siamese),
    SPHYNX (R.string.pet_breed_cat_sphynx, R.drawable.sphynx),
    NONE(R.string.util_blank, null)
}