package com.ica.android.service;

/**
 * 
 * @author Cigdem
 *
 */

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.AssetManager;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;

public class MachineLearning 
{
	
//	public static void main(String[] args)
	public int classifie(int fifteen, int thirty, String daypart)
	{
		Instances train_data = null;
		Instances test_data = null;

		try 
		{
			
			 // Declare two numeric attributes
			 Attribute Attribute1 = new Attribute("fifteen");
			 Attribute Attribute2 = new Attribute("thirty");
			 
			 // Declare a nominal attribute along with its values
			 FastVector fvNominalVal = new FastVector(4);
			 fvNominalVal.addElement("morning");
			 fvNominalVal.addElement("afternoon");
			 fvNominalVal.addElement("evening");
			 fvNominalVal.addElement("night");
			 Attribute Attribute3 = new Attribute("aNominal", fvNominalVal);
			 
			 
			 // Declare the class attribute along with its values
			 FastVector fvClassVal = new FastVector(2);
			 fvClassVal.addElement("negative");
			 fvClassVal.addElement("positive");
			 Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
			 
			 // Declare the feature vector
			 FastVector fvWekaAttributes = new FastVector(4);
			 fvWekaAttributes.addElement(Attribute1);    
			 fvWekaAttributes.addElement(Attribute2);    
			 fvWekaAttributes.addElement(Attribute3);    
			 fvWekaAttributes.addElement(ClassAttribute);
			
			
			// Create an empty training set
			 Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);           
			 // Set class index
			 isTrainingSet.setClassIndex(3);
			 
			 
			 // 3rd attribute is the day part use 1 = morning, 2 = afternoon, 3 = evening, 4 = night
			int datasett[][] = 
			{
					//fifteen, thirty, day part, classification  
					{1, 1, 1, 0}, {2, 2, 1, 0}, {3, 3, 1, 0}, {4, 4, 1, 1}, {1, 2, 1, 0}, {1, 3, 1, 0}, {1, 4, 1, 0}, {1, 5, 1, 1}, {2, 3, 1, 0}, {2, 4, 1, 0}, {2, 5, 1, 0}, {2, 6, 1, 1}, {3, 4, 1, 0}, {3, 5, 1, 0}, {3, 6, 1, 0}, {3, 7, 1, 1},
					
					{1, 1, 2, 0}, {2, 2, 2, 0}, {3, 3, 2, 0}, {4, 4, 2, 0}, {5, 5, 2, 1}, {1, 2, 2, 0}, {1, 3, 2, 0}, {1, 4, 2, 0}, {1, 5, 2, 0}, {1, 6, 2, 1}, {2, 3, 2, 0}, {2, 4, 2, 0}, {2, 5, 2, 0}, {2, 6, 2,0}, {2, 7, 2, 1}, {3, 4, 2, 0}, {3, 5, 2, 0}, {3, 6, 2, 0}, {3, 7, 2, 0}, {3, 8, 2, 1}, {4, 5, 2, 0}, {4, 6, 2, 0}, {4, 7, 2, 0}, {4, 8, 2, 1},
					
					{1, 1, 3, 0}, {2, 2, 3, 0}, {3, 3, 3, 0}, {4, 4, 3, 0}, {5, 5, 3, 1}, {1, 2, 3, 0}, {1, 3, 3, 0}, {1, 4, 3, 0}, {1, 5, 3, 0}, {1, 6, 3, 1}, {2, 3, 3, 0}, {2, 4, 3, 0}, {2, 5, 3, 0}, {2, 6, 3,0}, {2, 7, 3, 1}, {3, 4, 3, 0}, {3, 5, 3, 0}, {3, 6, 3, 0}, {3, 7, 3, 0}, {3, 8, 3, 1}, {4, 5, 3, 0}, {4, 6, 3, 0}, {4, 7, 3, 0}, {4, 8, 3, 1},
					
					{1, 1, 4, 0}, {2, 2, 4, 0}, {3, 3, 4, 1}, {1, 2, 4, 0}, {1, 3, 4, 0}, {1, 4, 4, 1}, {2, 3, 4, 0}, {2, 4, 4, 0}, {2, 5, 4, 1},
			};
			
			 for(int dim = 0; dim < datasett.length; dim++)
			 { 
				 Instance iExample = new SparseInstance(4);
				 
				 for (int obj = 0; obj < datasett[0].length - 2; obj++)
				 {
				 	iExample.setValue((Attribute)fvWekaAttributes.elementAt(obj), (double) (Integer)datasett[dim][obj]);
				 	iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), (double)(Integer)datasett[dim][2] == 1 ? "morning" : (double)(Integer)datasett[dim][2] == 2 ? "afternoon" : (double)(Integer)datasett[dim][2] == 3 ? "evening" : "night");
				 	iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), (double)(Integer)datasett[dim][3] == 1 ? "positive" : "negative");
				 }
				 isTrainingSet.add(iExample);
			 }
			 
			 train_data = isTrainingSet;
			 
			// Declare the feature vector
			 FastVector fvWekaAttributes2 = new FastVector(3);
			 fvWekaAttributes2.addElement(Attribute1);    
			 fvWekaAttributes2.addElement(Attribute2);    
			 fvWekaAttributes2.addElement(Attribute3);    
			
			
			// Create an empty training set
			 Instances isTestingSet = new Instances("Rel", fvWekaAttributes2, 10);           
			 // Set class index
			 isTrainingSet.setClassIndex(3);
			 
			// Create the instance
			 Instance iExample7 = new SparseInstance(3);
			 iExample7.setValue((Attribute)fvWekaAttributes.elementAt(0), fifteen);      
			 iExample7.setValue((Attribute)fvWekaAttributes.elementAt(1), thirty);      
			 iExample7.setValue((Attribute)fvWekaAttributes.elementAt(2), daypart);

			 
			// add the instance
			 isTestingSet.add(iExample7);


			 test_data = isTestingSet;
			 
			
			Attribute at2 = test_data.attribute(test_data.numAttributes() - 1);
			test_data.setClass(at2);

		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		 Classifier classifier = (Classifier)new RandomForest();
		
		int result = -1;
		try 
		{
			classifier.buildClassifier(train_data);

			for (int i = 0; i < test_data.numInstances(); i++) 
			{
				int classification = (int)classifier.classifyInstance(test_data.get(i));
				result = classification;
			}

		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
		/*try
		{
			ArrayList<Double> list_fold_results = new ArrayList<Double>();
			
			AttributeSelection filt = new AttributeSelection();
			CfsSubsetEval eval = new CfsSubsetEval();
			GreedyStepwise search = new GreedyStepwise();
			search.setSearchBackwards(true);
			filt.setEvaluator(eval);
			filt.setSearch(search);
			filt.setInputFormat(train_data);
			Instances new_training_data = Filter.useFilter(train_data, filt);
			
			for (int j = 0; j < folds; j++) {
				Instances part_of_training_data = new_training_data.trainCV(folds, j);
				Instances part_of_testing_data = new_training_data.testCV(folds, j);
				
				String[] options = new String[6];
				options[0] = "-I"; //Number of trees to build.
				options[1] = "5000";
				options[2] = "-K"; //Number of features to consider
				options[3] = "85";
				options[4] = "-depth"; //The maximum depth of the trees, 0 for unlimited.
				options[5] = "10";
				
				Classifier classifier = (Classifier)new RandomForest();
				classifier.setOptions(options);
				classifier.buildClassifier(part_of_training_data);
				
				for (int i = 0; i < part_of_testing_data.numInstances(); i++) 
				{
					int classification = (int) classifier.classifyInstance(part_of_testing_data.get(i));
					//System.out.println(i+1 +","+ classification);
				}
			
				Evaluation e = new Evaluation(part_of_training_data);
				e.evaluateModel(classifier, part_of_testing_data);
			
				double pctCorrect = e.pctCorrect();
				list_fold_results.add(pctCorrect);
									
			}
			System.out.println("accuracies " + ": "+ list_fold_results.toString());					

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}
}
