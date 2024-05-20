class ApiFeatures {
    constructor(mongooseQuery, mainQuery, type = "find") {
        this.mongooseQuery = mongooseQuery;
        this.mainQuery = mainQuery;
        this.type = type;
    }

    filter() {
        // 1-) Filtering
        const queryObj = { ...this.mainQuery };
        const unwanted = ["page", "sort", "limit", "count", "fileds", "keyword"];
        unwanted.forEach((unwanted) => delete queryObj[unwanted]);

        // 2-) Converting gt, gte, lt, lte to $gt, $gte, $lt, $lte
        let queryStr = JSON.stringify(queryObj);
        queryStr = queryStr.replace(/\b(gte|gt|lte|lt)\b/g, (match) => `$${match}`);
        let query = JSON.parse(queryStr);

        // 3-) Converting string to integer
        let array = ["$lt", "$gt", "$lte", "$gte"];
        for (const att in query) {
           if(att !== "paymentAt"){
               for (const key in query[att]) {
                   if (array.includes(key)) {
                       query[att][key] = query[att][key] * 1;
                   }
               }
           }
        }

        if (this.type === "find")
            this.mongooseQuery = this.mongooseQuery.find(query);
        else this.mongooseQuery = this.mongooseQuery.match(query);

        return this;
    }

    sort() {
        // 1-) Sorting
        if (this.mainQuery.sort) {
            const sortBy = this.mainQuery.sort.split(",").join(" ");
            this.mongooseQuery = this.mongooseQuery.sort(sortBy);
        } else {
            this.mongooseQuery = this.mongooseQuery.sort("-createdAt");
        }

        return this;
    }

    search(serachField = "name") {
        // 1-) Search
        if (this.mainQuery.keyword) {
            let searchQuery = {};

            searchQuery[serachField] = {
                $regex: this.mainQuery.keyword,
                $options: "i",
            };

            if (this.type === "find")
                this.mongooseQuery = this.mongooseQuery.find(searchQuery);
            else this.mongooseQuery = this.mongooseQuery.match(searchQuery);
        }
        return this;
    }

    fileds() {
        // 1-) fileds
        if (this.mainQuery.fileds) {
            const fileds = this.mainQuery.fileds.split(",").join(" ");
            if (this.type === "find")
                this.mongooseQuery = this.mongooseQuery.select(fileds);
            else this.mongooseQuery = this.mongooseQuery.project(fileds);
        }

        return this;
    }

    paginate(count) {
        const pagination = {};

        if (typeof count === "object") count = count[0].total;

        // 1-) Pagination
        const page = this.mainQuery.page * 1 || 1;
        const limit = this.mainQuery.limit * 1 || 50;
        const skip = (page - 1) * limit;
        const endIndex = page * limit;

        pagination.currentPage = page;
        pagination.limit = limit;
        pagination.numberOfPages = Math.ceil(count / limit);
        pagination.numberOfDocemunts = count;

        if (skip > 0) pagination.prev = page - 1;
        if (endIndex < count) pagination.next = pagination.currentPage + 1;

        this.pagination = pagination;
        this.mongooseQuery = this.mongooseQuery.skip(skip).limit(limit);
        return this;
    }
}

module.exports = ApiFeatures;