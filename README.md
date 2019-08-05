# PetApp-Android
 #### refactored-mvc-unit-test-and-instrumentation-test
 Below changes made:
 1. androidTest -> MainActivityTest has modified
 
 In master branch implementation was done using ListView. But in MVC model I have introduced
 some more third-party library and changed ListView to RecyclerView
 So modified some part of code to pass Instrumentation test.
 
 2. Added unit test DateUtilsTest
 
 During implementation I have noticed, there are few conditions it may cause failure in future.
 So after implementing test case I have modified DateUtils code aswell.
 For more details please refer the source code.
 
 Summary of test case run:
 ![Summary of UnitTest](https://github.com/developer-anees/PetApp-Android/blob/refactored-mvc-unit-test-and-instrumentation-test/app/src/main/assets/unit_test_dateutils_result.png)
 