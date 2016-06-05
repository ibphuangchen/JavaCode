package forPractise;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

class  MergeSort<T extends Comparable<T>> extends RecursiveAction{
	private List<T> array;	
	int start;
	int end;
	public MergeSort(List<T> array, int start, int end){
		this.array=array;
		this.start=start;
		this.end=end;
	}
	@Override
	protected void compute() {
		if(end-start==1)
			return;
		MergeSort<T> newTask=new MergeSort<T>(array, (end+start)/2, end);
		newTask.fork();
		new MergeSort<T>(array, start, (end+start)/2).compute();
		newTask.join();
		List<T> helperArray=new ArrayList<T>(array);
		int i=start;
		int j=(end+start)/2;
		int idx=start;
		while(i<(start+end)/2 && j<end){
			T value= (helperArray.get(i).compareTo(helperArray.get(j))<0?helperArray.get(i++):helperArray.get(j++));
			array.set(idx++, value);
		}
		while(i<(end+start)/2){
			array.set(idx++, helperArray.get(i++));
		}
		while(j<end){
			array.set(idx++, helperArray.get(j++));
		}
	}
}

class cMergeSort<T extends Comparable<T>>{
	private List<T> array;	
	int start;
	int end;
	public cMergeSort(List<T> array, int start, int end){
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	protected void doSorting() {
		if(end-start==1)
			return;
		new cMergeSort<T>(array, (end+start)/2, end).doSorting();
		new cMergeSort<T>(array, start, (end+start)/2).doSorting();
		List<T> helperArray=new ArrayList<>(array);
		int i=start;
		int j=(end+start)/2;
		int idx=start;
		while(i<(start+end)/2 && j<end){
			T value= (helperArray.get(i).compareTo(helperArray.get(j))<0?helperArray.get(i++):helperArray.get(j++));
			array.set(idx++, value);
		}
		while(i<(end+start)/2)
			array.set(idx++, helperArray.get(i++));
		while(j<end)
			array.set(idx++,  helperArray.get(j++));
	}
}

public class Cur2 {
	public static void main(String...args){
		List<Double> testArray1 = DoubleStream.generate(new Random()::nextDouble).limit(100000).mapToObj(e->e).collect(Collectors.toList());
		//System.out.println(testArray1);
		Instant start1=Instant.now();
		new cMergeSort<Double>(testArray1, 0, testArray1.size()).doSorting();
		System.out.println("W/o parallel, your sorting runs for: "+Duration.between(start1, Instant.now()));
		List<Double> testArray2 = DoubleStream.generate(new Random()::nextDouble).limit(100000).mapToObj(e->e).collect(Collectors.toList());
		Instant start2=Instant.now();
		ForkJoinPool fj=new ForkJoinPool();
		//System.out.println(testArray2);
		fj.invoke(new MergeSort<Double>(testArray2, 0, testArray2.size()));
		//System.out.println(testArray2);
		System.out.println("W/ parallel, your sorting runs for: "+Duration.between(start2, Instant.now()));
	}

}
