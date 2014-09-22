FragmentMaster
==============

FragmentMaster is a library allows you easily develop an Android application which only navigated by Fragments.



Features
========

  * Pass data and receive result between two fragments.
  * Swipe right to navigate back.
  * Custom transform animation.



Usage
=====

1. MasterFragment

   In FragmentMaster, every "page" is subclass of **MasterFragment** (Or other IMasterFragment, like **MasterListFragment**) not **Activity**.

   1. You can start a MasterFragment as start a Activity:
   ```Java
   // start YourMasterFragment
   Request request = new Request(YourMasterFragment.class);
   startFragment(request);
   ```

   2. You can pass data within two MasterFragments:
   ```Java
   // pass data.
   Request request = new Request(YourMasterFragment.class);
   request.putExtra(EXTRA_KEY, "Extra data");
   startFragment(request);
   ```

   3. And get extra data in YourMasterFragment:
   ```Java
   Request request = getRequest();
   String extraData = request.getStringExtra(EXTRA_KEY);
   ```

2. Install FragmentMaster

   Subclass **MasterActivity**, it is the host of all MasterFragments. And in its ```onCreate()``` add the codes below after ```setContentView()``` method:
   
   ```Java
   setContentView(R.layout.master_activity);
   FragmentMaster fragmentMaster = getFragmentMaster();
   fragmentMaster.install(R.id.container, new Request(Home.class), true);
   ```

   The first parameter of ```install()``` is your MasterFragment's container. All started MasterFragment will be added to this container.
   The second parameter is the decription of the home MasterFragment. The home MasterFragment will be started once the host MasterActivity is started.
   If the third parameter is true, the host MasterActivity will be finished when the home MasterFragment is Finished.

3. PageAnimator

   FragmentMaster provides an API for you to add custom animation for each MasterFragment. The animation will be performed when starting, finishing and dragging by user.

   1. Subclass PageAnimator and implement two methods:
   ```Java
   // Transform the background page.
   protected abstract void transformBackgroundPage(View page, float position,
boolean enter);
   ```
   
   ```Java
   // Transform the foreground page.
   protected abstract void transformForegroundPage(View page, float position,
boolean enter);
   ```

   2. Override onCreatePageAnimator method of MasterFragment to provide your custom PagerAnimator:
   ```Java
   @Override
   public PageAnimator onCreatePageAnimator() {
       // return your own PageAnimator.
       return YOUR_CUSTOM_ANIMATOR;
   }
   ```
   [There are some custom PageAnimators in the Sample project.](https://github.com/fengdai/FragmentMaster/tree/master/fragmentmaster-samples/src/com/fragmentmaster/sample/pageanimator)


Still missing
=============

  * An inner fragment ActionBar.



License
=======

    Copyright 2014 Feng Dai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
