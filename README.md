# Optimizing Computer Networks Communication with the Band Collocation Problem: A Variable Neighborhood Search Approach

The Band Collocation Problem appears in the context of problems for optimizing telecommunication networks with the aim of solving some concerns related to the original Bandpass Problem and to present a more realistic approximation to be solved. This problem is interesting to optimize the cost of networks with several devices connected, such as networks with several embedded systems transmitting information among them. Despite the real-world applications of this problem, it has been mostly ignored from a heuristic point of view, with the Simulated Annealing algorithm being the best method found in the literature. In this work, three Variable Neighborhood Search (VNS) variants are presented, as well as three neighborhood structures and a novel optimization based on Least Recently Used cache, which allows the algorithm to perform an efficient evaluation of the objective function. The extensive experimental results section shows the superiority of the proposal with respect to the best previous method found in the state-of-the-art, emerging VNS as the most competitive method to deal with the Band Collocation Problem.

Paper link: <https://doi.org/10.3390/electronics9111860>
Impact Factor: 2.412
Quartile: Q2

## Datasets

The official webpage are down but you can download the original instances (excel format) or txt parsed instances used by us in the article.

* [Excel BCP Optimal Known Instances Initial Values](excel/BCPOptimalKnownInstancesInitialValues.xlsx)
* [BCP UnKnown Instances](excel/BCPUnKnownInstances.xlsx)
* [BCP UnKnown Instances Initial Values](excel/BCPUnKnownInstancesInitialValues.xlsx)

All txt format instances can be found in instances folder.

## Executable

You can just run the BandCollocation.jar as follows.

```
java -jar BandCollocation.jar
```

If you want new instances just replace folder instances.
Solution folder contains de output per instance and folder experiments has all grouped in an excel file.

## Cite

Please cite our paper if you use it in your own work:

Bibtext
```
@article{LozanoOsorio2020,
  doi = {10.3390/electronics9111860},
  url = {https://doi.org/10.3390/electronics9111860},
  year = {2020},
  month = nov,
  publisher = {{MDPI} {AG}},
  volume = {9},
  number = {11},
  pages = {1860},
  author = {Isaac Lozano-Osorio and Jesus Sanchez-Oro and Miguel {\'{A}}ngel Rodriguez-Garcia and Abraham Duarte},
  title = {Optimizing Computer Networks Communication with the Band Collocation Problem: A Variable Neighborhood Search Approach},
  journal = {Electronics}
}
```

MDPI and ACS Style
```
Lozano-Osorio, I.; Sanchez-Oro, J.; Rodriguez-Garcia, M.Á.; Duarte, A. Optimizing Computer Networks Communication with the Band Collocation Problem: A Variable Neighborhood Search Approach. Electronics 2020, 9, 1860.
```

AMA Style
```
Lozano-Osorio I, Sanchez-Oro J, Rodriguez-Garcia MÁ, Duarte A. Optimizing Computer Networks Communication with the Band Collocation Problem: A Variable Neighborhood Search Approach. Electronics. 2020; 9(11):1860.
```

Chicago/Turabian Style
```
Lozano-Osorio, Isaac; Sanchez-Oro, Jesus; Rodriguez-Garcia, Miguel Á.; Duarte, Abraham. 2020. "Optimizing Computer Networks Communication with the Band Collocation Problem: A Variable Neighborhood Search Approach" Electronics 9, no. 11: 1860.
```
