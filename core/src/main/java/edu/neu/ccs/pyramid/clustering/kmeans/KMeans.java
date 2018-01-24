package edu.neu.ccs.pyramid.clustering.kmeans;

import edu.neu.ccs.pyramid.dataset.DataSet;
import edu.neu.ccs.pyramid.util.ArgMin;
import edu.neu.ccs.pyramid.util.MathUtil;
import edu.neu.ccs.pyramid.util.Sampling;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

import java.util.List;
import java.util.stream.IntStream;

public class KMeans {
    private int numComponents;
    private Vector[] centers;
    private DataSet dataSet;
    private int[] assignments;

    public KMeans(int numComponents, DataSet dataSet) {
        this.numComponents = numComponents;
        this.dataSet = dataSet;
        this.centers = new DenseVector[numComponents];
        this.assignments = new int[dataSet.getNumDataPoints()];
    }

    public void iterate(){
        assign();
        updateCenters();
    }

    public int getNumComponents() {
        return numComponents;
    }

    public Vector[] getCenters() {
        return centers;
    }

    public int[] getAssignments() {
        return assignments;
    }

    private void updateCenters(){
        IntStream.range(0,numComponents).parallel()
                .forEach(this::updateCenters);
    }

    public void randomInitialize(){
        for (int k=0;k<numComponents;k++){
            int dataIndex = Sampling.intUniform(0,dataSet.getNumDataPoints()-1);
            centers[k] = dataSet.getRow(dataIndex);
        }
    }

    public void kmeansPlusPlusInitialize(){
        KMeansPlusPlus kMeansPlusPlus = new KMeansPlusPlus(this.numComponents,this.dataSet);
        kMeansPlusPlus.initialize();
        List<Vector> c = kMeansPlusPlus.getCenters();
        for (int k=0;k<numComponents;k++){
            centers[k] = c.get(k);
        }
    }

    public double objective(){
        return IntStream.range(0, dataSet.getNumDataPoints()).parallel()
                .mapToDouble(this::objective).sum();
    }
    private double objective(int i){
        return Math.pow(distance(dataSet.getRow(i),centers[assignments[i]]),2);
    }

    private void updateCenters(int k){
        Vector center = new DenseVector(dataSet.getNumFeatures());
        double count = 0;
        for (int i=0;i<dataSet.getNumDataPoints();i++){
            if (assignments[i]==k){
                Vector instance = dataSet.getRow(i);
                for (int j=0;j<instance.size();j++){
                    center.set(j, center.get(j)+instance.get(j));
                }
                count += 1;
            }
        }
        center = center.divide(count);
        centers[k] = center;
    }

    private void assign(int i){
        Vector vector = dataSet.getRow(i);
        double[] distances = IntStream.range(0,numComponents).mapToDouble(k->distance(vector, centers[k]))
                .toArray();
        int assigned =  ArgMin.argMin(distances);
        assignments[i] = assigned;
    }


    private void assign(){
        IntStream.range(0, dataSet.getNumDataPoints()).parallel()
                .forEach(this::assign);
    }

    static double distance(Vector vector1, Vector vector2){
        double sum = 0;
        for (int i=0;i<vector1.size();i++){
            double diff = (vector1.get(i)-vector2.get(i));
            sum += diff*diff;
        }
        return Math.pow(sum,0.5);
    }

}