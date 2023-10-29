import {
    EventPaymentStatus,
    IEvent,
    IRegisteredUser,
    TPaymentStatusValidate,
} from '../database/models/event';
import {
    BadRequestError,
    NotFoundError,
    ResourceConflictError,
} from '../lib/custom-errors/class-errors';
import DateUtils from '../lib/date';
import EventMapper from '../lib/mappers/event-mapper';
import EventRepository, {
    TCreateEvent,
    TEventListQuery,
} from '../repositories/event-repository';
import UserRepository from '../repositories/user-repository';

interface IEventService {
    createEvent: (payload: TCreateEvent) => Promise<IEvent>;
    getYearlyEvents: (
        year: string,
        uid: string
    ) => Promise<Record<string, number>[]>;
    update: (payload: Partial<TCreateEvent>) => Promise<IEvent>;
    getEvent: (id: string) => Promise<IEvent>;
}

class EventRegistration {
    private _event: IEventService;

    constructor(event: IEventService) {
        this._event = event;
    }
    public async registerCyclist(
        payload: IRegisteredUser & { eventId: string; modifiedAt: string }
    ) {
        try {
            const event = await this._event.getEvent(payload.eventId);
            if (!event) throw new NotFoundError('Event not found.');

            const hasUser = event.registeredUser?.find(
                (user: IRegisteredUser) => user.user.id === payload.user.id
            );
            if (hasUser) {
                throw new ResourceConflictError('User already registered.');
            }

            event.registeredUser?.push(payload);
            payload.status = EventPaymentStatus.UNPAID;
            event.modifiedAt = payload.modifiedAt;
            return this._event.update(event);
        } catch (error) {
            throw error;
        }
    }
}

class EventService implements IEventService {
    private _repository = new EventRepository();
    private _mapper = new EventMapper();
    private _user = new UserRepository();
    private _dateUtils = DateUtils.getInstance();

    public async createEvent(payload: TCreateEvent) {
        try {
            const mappedEvent = this._mapper.createEvent(payload);
            return (await this._repository.create(mappedEvent)) as IEvent;
        } catch (error) {
            throw error;
        }
    }

    public async getYearlyEvents(year: string) {
        try {
            return await this._repository.getEventsCount(year);
        } catch (error) {
            throw error;
        }
    }

    public async update(payload: Partial<TCreateEvent>) {
        try {
            await this.getEvent(payload.id as string);
            const updatedEvent = await this._repository.update(payload);
            return updatedEvent as IEvent;
        } catch (error) {
            throw error;
        }
    }

    public async getCurrentEvent() {
        return await this._repository.getByEventDate(
            this._dateUtils
                .getMomentInstance(new Date())
                .tz('Asia/Manila')
                .format('YYYY-MM-DD')
        );
    }

    public async getEvent(id: string) {
        try {
            const event = (await this._repository.findEventById(
                id
            )) as IEvent & {
                isDone: boolean;
                isNow: boolean;
            };
            if (!event) throw new NotFoundError('Event not found.');

            const author = await this._user.getUserById(event.author.id);
            if (!author) {
                throw new NotFoundError('Author does not exits.');
            }
            event.author = {
                id: author.id,
                firstname: author.firstname,
                lastname: author.lastname,
                photoUrl: author.photoUrl,
            };

            if (event!.registeredUser!.length > 0) {
                event.registeredUser = await Promise.all(
                    event.registeredUser?.map(async (user) => {
                        const userData = await this._user.getUserById(
                            user.user.id
                        );
                        if (!userData) {
                            throw new NotFoundError(
                                'Registered user does not exits.'
                            );
                        }
                        return {
                            status: user.status,
                            createdAt: user.createdAt,
                            eventId: user.eventId,
                            paymentUrl: user.paymentUrl,
                            paymentType: user.paymentType,
                            user: {
                                id: userData.id,
                                firstname: userData.firstname,
                                lastname: userData.lastname,
                                photoUrl: userData.photoUrl,
                            },
                        };
                    }) as []
                );
            }

            event.isDone = this.getEventValidity(event.endTime);
            const startTime = this._dateUtils.getMomentInstance(
                new Date(event.startTime)
            );
            const endTime = this._dateUtils.getMomentInstance(event.endTime);
            const current = this._dateUtils
                .getMomentInstance()
                .tz('Asia/Manila');
            event.isNow =
                current.isSameOrAfter(startTime) &&
                current.isSameOrBefore(endTime);

            return event as IEvent;
        } catch (error) {
            throw error;
        }
    }

    public async paymentStatusValidate(payload: TPaymentStatusValidate) {
        const event = await this._repository.findEventById(payload.eventId);
        if (!event) throw new NotFoundError('Event does not exists.');

        const paid = event.registeredUser?.every(
            (registree) =>
                registree.user.id === payload.userId &&
                registree.status === EventPaymentStatus.PAID
        );

        return {
            paid,
        };
    }

    public async updatePaymentStatus(
        payload: TPaymentStatusValidate & { status: EventPaymentStatus }
    ) {
        const event = await this._repository.findEventById(payload.eventId);
        if (!event) throw new NotFoundError('Event does not exists.');

        if (event.registeredUser!.length === 0) {
            throw new BadRequestError('No registered users.');
        }

        const eventUser = event.registeredUser!.findIndex(
            (obj) => obj.user?.id === payload.userId
        );

        event.registeredUser![eventUser as number].status = payload.status;
        if (payload.status === EventPaymentStatus.REJECTED) {
            event.registeredUser![eventUser as number].paymentUrl = '';
        }
        await this._repository.update(event);
        return event.registeredUser![eventUser];
    }

    private getEventValidity(eventDate: string) {
        return this._dateUtils
            .getMomentInstance(new Date())
            .isSameOrAfter(new Date(eventDate));
    }

    public async deleteEvents(ids: string) {
        try {
            const deletedEvents = await Promise.all(
                ids.split(',').map(async (id) => {
                    return await this._repository.deleteEvent(id);
                })
            );

            return deletedEvents;
        } catch (error) {
            throw error;
        }
    }

    public async getEvents(query: TEventListQuery) {
        try {
            const events = await this._repository.getEventList(query);
            if (events.length === 0) return events;

            const mappedEvents = await Promise.all(
                events.map(async (event) => {
                    const author = await this._user.getUserById(
                        event.author.id
                    );
                    if (!author) {
                        throw new NotFoundError('Author does not exits.');
                    }
                    event.author = {
                        id: author.id,
                        firstname: author.firstname,
                        lastname: author.lastname,
                        photoUrl: author.photoUrl,
                    };

                    if (event!.registeredUser!.length > 0) {
                        event.registeredUser = await Promise.all(
                            event.registeredUser?.map(async (user) => {
                                const userData = await this._user.getUserById(
                                    user.user.id
                                );
                                if (!userData) {
                                    throw new NotFoundError(
                                        'Registered user does not exits.'
                                    );
                                }
                                return {
                                    status: user.status,
                                    createdAt: user.createdAt,
                                    eventId: user.eventId,
                                    paymentUrl: user.paymentUrl,
                                    paymentType: user.paymentType,
                                    user: {
                                        id: userData.id,
                                        firstname: userData.firstname,
                                        lastname: userData.lastname,
                                        photoUrl: userData.photoUrl,
                                    },
                                };
                            }) as []
                        );
                    }
                    return {
                        ...event,
                        isDone: this.getEventValidity(event.eventDate),
                    };
                })
            );
            return mappedEvents;
        } catch (error) {
            throw error;
        }
    }
}

export { EventService, EventRegistration };
