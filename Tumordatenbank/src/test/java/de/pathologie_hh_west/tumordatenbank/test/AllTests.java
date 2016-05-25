package de.pathologie_hh_west.tumordatenbank.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DAOTest.class, ExcelPackageTest.class })
public class AllTests {

}
