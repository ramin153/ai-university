from maps import Map
import math
import time


class Search:

    def __init__(self):
        self.count = 4
    def getGoals(self,map:list)->list:
        goals = []
        for i in range(len(map)):
            for j in range(len(map[i])):
                if map[i][j] == 20:
                    goals.append((j,i))
        return goals
    def dfs(self, m: Map) -> list:
        start_time = time.time()
        return_list = []
        goals = self.getGoals(m.get_matrix())
        self.dfs_req(m, m.get_matrix(), m.me, return_list,goals)

        print('DFS Search')
        print('Duration:', time.time() - start_time)
        return return_list

    def dfs_req(self, m: Map, my_map: list, location: tuple, final_result: list,goals : list):
        try:
            successors = m.get_successors(location[0], location[1])
            for item in successors:
                state = my_map[item[1]][item[0]]
                if state == 57 or my_map[item[1]][item[0]] == 20:
                    final_result.append(item)

                    if my_map[item[1]][item[0]] == 20:
                        for x in goals:
                            if (x[1] ,x[0]) == (item[1], item[0]):
                                goals.remove(x)

                    my_map[item[1]][item[0]] = 0
                    if len(goals) == 0:
                        return True
                    if self.dfs_req(m, my_map, item, final_result,goals):
                        return True
        except:
            pass

        return False

    def bfs(self, m: Map) -> list:

        start_time = time.time()
        final_result = []
        move_list = [m.me]
        my_map = m.get_matrix()
        my_map[m.me[1]][m.me[0]] = 0
        goals = self.getGoals(m.get_matrix())
        while goals:
            location = move_list.pop(0)
            if location != m.me:
                final_result.append(location)
            if my_map[location[1]][location[0]] == 40:
                for x in goals:
                    if (x[1], x[0]) == (location[1], location[0]):
                        goals.remove(x)


            if len(goals) == 0:
                break

            try:
                successors = m.get_successors(location[0], location[1])
                for item in successors:
                    state = my_map[item[1]][item[0]]
                    if state == 57 or state == 20:
                        move_list.append(item)
                        if my_map[item[1]][item[0]] == 20:
                            my_map[item[1]][item[0]] = 40
                        else:
                            my_map[item[1]][item[0]] = 0

            finally:
                pass

        print('BFS Search')
        print('Duration:', time.time() - start_time)
        return final_result

    def uniform_cost(self, m: Map) -> list:
        start_time = time.time()
        final_result = []
        my_map = m.get_matrix()
        pq = MyPriorityQueue([[0 for j in range(len(my_map[i]))] for i in range (len(my_map))])
        pq.insert((0, m.me))
        goals = self.getGoals(m.get_matrix())
        while goals:
            val ,loc = pq.get()
            state = my_map[loc[1]][loc[0]]

            if not (state == 57 or my_map[loc[1]][loc[0]] == 20 or my_map[loc[1]][loc[0]] == 67):
                continue

            if m.me != loc:
                final_result.append(loc)

            if state == 20:
                for x in goals:
                    if (x[1], x[0]) == (loc[1], loc[0]):
                        goals.remove(x)


            my_map[loc[1]][loc[0]] = 0

            try:
                successors = m.get_successors(loc[0], loc[1])
                for item in successors:
                    state = my_map[item[1]][item[0]]
                    if state == 57 or state == 20:
                        pq.insert((1+val,item))


            finally:
                pass




        # You Code Here

        print('Uninformed Search')
        print('Duration:', time.time() - start_time)
        return final_result

    def get_euclidean_heuristics(self, m: Map) -> list:
        goals = m.goals
        result = m.get_matrix()

        def best_heuristics( x: int, y: int, goal: list):
            best = -1

            for i in goal:
                new_one =  math.sqrt(math.pow(i[1] - x, 2) + math.pow(i[0] - y, 2))

                if best == -1 or best > new_one:
                    best = new_one

            return best

        for i in range(len(result)):

            for j in range(len(result[i])):
                if result[i][j] == 57:
                    result[i][j] = best_heuristics(i, j, goals)
                elif result[i][j] == 20:
                    result[i][j] = 0
                else:
                    result[i][j] = -1


        # Your Code here
        # Hint: https://en.wikipedia.org/wiki/Euclidean_distance#Two_dimensions
        # Hint: http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
        return  result




    def a_star(self, m: Map, heuristics: list) -> list:


        start_time = time.time()
        final_result = []
        my_map = m.get_matrix()
        pq = MyPriorityQueue(heuristics)
        pq.insert((0, m.me))

        goals = self.getGoals(m.get_matrix())
        while goals:
            val, loc = pq.get()
            state = my_map[loc[1]][loc[0]]
            if not (state == 57 or my_map[loc[1]][loc[0]] == 20 or my_map[loc[1]][loc[0]] == 67):
                continue

            if m.me != loc:
                final_result.append(loc)

            if state == 20:
                for x in goals:
                    if (x[1], x[0]) == (loc[1], loc[0]):
                        goals.remove(x)


            my_map[loc[1]][loc[0]] = 0

            try:
                successors = m.get_successors(loc[0], loc[1])
                for item in successors:
                    state = my_map[item[1]][item[0]]
                    if state == 57 or state == 20:
                        pq.insert((1 + val, item))


            finally:
                pass

        print('A* Search')
        print('Duration:', time.time() - start_time)
        return final_result


class MyPriorityQueue:
    def __init__(self, heuristics: list):
        """
        every item is tuple
        first is value
        second one is location

        """

        self.queue = []
        self.heuristics = heuristics

    def __str__(self):
        return ' '.join([str(i) for i in self.queue])

    def isEmpty(self):
        return len(self.queue) == 0

    def insert(self, data):
        is_add = False
        for i in range(len(self.queue)):
            if data[1] == self.queue[i][1]:
                if data[0] < self.queue[i][0]:
                    self.queue.remove(self.queue[i])
                else:
                    is_add = True
                break

        if is_add:
            return

        for i in range(len(self.queue)):
            if data[0] + self.heuristics[data[1][1]][data[1][0]] < self.queue[i][0] + self.heuristics[self.queue[i][1][1]][self.queue[i][1][0]] :
                self.queue.insert(i, data)
                is_add = True
                break

        if is_add != True:
            self.queue.append(data)


    def get(self):
        try:
            return self.queue.pop(0)
        except IndexError:
            print()
            exit()
