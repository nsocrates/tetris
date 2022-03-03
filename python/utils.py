flatten = lambda l: [item for sublist in l for item in sublist]
matrix = lambda a, m, n: [[a] * m for _ in range(n)]
pick = lambda d, t: [d[k] for k in t]
