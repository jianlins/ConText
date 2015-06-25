package edu.utah.bmi.context.common;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class permit to modify values in a Enum variable
 * http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
 *
 * @author Jérôme Kehrli, modified by Jianlin Shi with an additional method that can takes a String List
 */
public class DynamEnumOper {

    private static ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();

    private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException {

        // let's make the field accessible
        field.setAccessible(true);

        // next we change the modifier in the Field instance to
        // not be final anymore, thus tricking reflection into
        // letting us modify the static final field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        int modifiers = modifiersField.getInt(field);

        // blank out the final bit in the modifiers int
        modifiers &= ~Modifier.FINAL;
//        modifiers= Modifier.STATIC;
        modifiersField.setInt(field, modifiers);
        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
        blankField(enumClass, "enumConstants"); // IBM JDK
    }

    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass)
            throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static Object makeEnum(Class<?> enumClass, String value, int ordinal) throws Exception {
        Object[] parms = new Object[2];
        parms[0] = value;
        parms[1] = Integer.valueOf(ordinal);
        ConstructorAccessor ca = getConstructorAccessor(enumClass);
        Object instance = ca.newInstance(parms);
        return enumClass.cast(instance);
    }

    /**
     * Add an enum instance to the enum class given as argument
     *
     * @param <T>      the type of the enum (implicit)
     * @param enumType the class of the enum to be modified
     * @param enumName the name of the new enum instance to be added to the class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void addEnums(Class<T> enumType, String enumName) {

        // 0. Sanity checks
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        // 1. Lookup "$VALUES" holder in enum class and get previous enum instances
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        try {

            // 2. Copy it
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));

            // 3. build new enum
            T newValue = (T) makeEnum(enumType, // The target enum class
                    enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                    values.size()); // could be used to pass values to the enum constuctor if needed

            // 4. add new value
            values.add(newValue);

            // 5. Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. Clean enum cache
            cleanEnumCache(enumType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Add an enum instance to the enum class given as argument
     *
     * @param <T>       the type of the enum (implicit)
     * @param enumType  the class of the enum to be modified
     * @param enumNames the names of the new enum instance to be added to the class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> void addEnums(Class<T> enumType, List<String> enumNames) {

        // 0. Sanity checks
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        // 1. Lookup "$VALUES" holder in enum class and get previous enum instances
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        AccessibleObject.setAccessible(new Field[]{valuesField}, true);


        addEnumNames(enumType, valuesField, enumNames);


    }

    private static <T extends Enum<?>> void addEnumNames(Class<T> enumType, Field valuesField, List<String> enumNames) {
        try {
            // 2. Copy it
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
            for (String enumName : enumNames) {
                // 3. build new enum
                T newValue = (T) makeEnum(enumType, // The target enum class
                        enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                        values.size()); // could be used to pass values to the enum constuctor if needed

                // 4. add new value
                values.add(newValue);
            }
            // 5. Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. Clean enum cache
            cleanEnumCache(enumType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static <T extends Enum<?>> void addEnumNames(Class<T> enumType, Field valuesField, String[] enumNames) {
        try {
            // 2. Copy it
            T[] previousValues = (T[]) valuesField.get(enumType);
            List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
            for (String enumName : enumNames) {
                // 3. build new enum
                T newValue = (T) makeEnum(enumType, // The target enum class
                        enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
                        values.size()); // could be used to pass values to the enum constuctor if needed

                // 4. add new value
                values.add(newValue);
            }
            // 5. Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            // 6. Clean enum cache
            cleanEnumCache(enumType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}